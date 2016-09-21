package net.simpleframework.module.news.impl;

import static net.simpleframework.common.I18n.$m;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.EFilterRelation;
import net.simpleframework.ado.FilterItem;
import net.simpleframework.ado.FilterItems;
import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.db.common.ExpressionValue;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.ado.lucene.ILuceneManager;
import net.simpleframework.ado.lucene.LuceneDocument;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.TimePeriod;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.ctx.task.ExecutorRunnable;
import net.simpleframework.module.common.content.AbstractCategoryBean;
import net.simpleframework.module.common.content.AbstractContentBean.EContentStatus;
import net.simpleframework.module.common.content.impl.AbstractContentService;
import net.simpleframework.module.news.INewsContextAware;
import net.simpleframework.module.news.INewsService;
import net.simpleframework.module.news.bean.News;
import net.simpleframework.module.news.bean.NewsCategory;
import net.simpleframework.module.news.bean.NewsStat;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsService extends AbstractContentService<News>
		implements INewsService, INewsContextAware {

	@Override
	public IDataQuery<News> queryBeans(final AbstractCategoryBean oCategory, final ID domainId,
			final EContentStatus status, final TimePeriod timePeriod, FilterItems filterItems,
			final ColumnData... orderColumns) {
		if (filterItems == null) {
			filterItems = FilterItems.of();
		}

		if (oCategory != null) {
			filterItems.addEqual("categoryId", oCategory.getId());
		}

		if (domainId != null) {
			filterItems.add(FilterItem.isNull("domainId").setLbracket(true));
			filterItems.add(FilterItem.or("domainId", domainId).setRbracket(true));
		}

		if (timePeriod != null) {
			filterItems.addEqual("createdate", timePeriod);
		}

		if (status != null) {
			filterItems.addEqual("status", status);
		} else {
			filterItems.addNotEqual("status", EContentStatus.delete);
		}
		return queryByParams(filterItems, orderColumns);
	}

	@Override
	public IDataQuery<News> queryBeans(final AbstractCategoryBean oCategory,
			final EContentStatus status, final TimePeriod timePeriod, final FilterItems filterItems,
			final ColumnData... orderColumns) {
		return queryBeans(oCategory, null, status, timePeriod, filterItems, orderColumns);
	}

	@Override
	public News getBeanByName(final String name) {
		return StringUtils.hasText(name) ? getBean("cname=?", name) : null;
	}

	@Override
	public IDataQuery<News> queryImageNews(final NewsCategory oCategory) {
		return queryBeans(oCategory, EContentStatus.publish, null, FilterItems.of("imageMark", true),
				ORDER_CREATEDATE);
	}

	@Override
	public IDataQuery<News> queryVideoNews(FilterItems items, final ID userId,
			final NewsCategory category, final ColumnData... orderColumns) {
		if (items == null) {
			items = FilterItems.of();
		}
		items.addEqual("videoMark", true);
		if (userId != null) {
			items.addEqual("userid", userId);
		}
		return queryBeans(category, EContentStatus.publish, null, items,
				ArrayUtils.isEmpty(orderColumns) ? ORDER_CREATEDATE : orderColumns);
	}

	private final ColumnData[] RECOMMENDATION_ORDER_COLUMNS = ArrayUtils.add(
			new ColumnData[] { ColumnData.DESC("rlevel") }, ColumnData.class,
			getDefaultOrderColumns());

	@Override
	public IDataQuery<News> queryRecommendBeans(final NewsCategory category,
			final TimePeriod timePeriod) {
		return queryBeans(category, EContentStatus.publish, timePeriod,
				FilterItems.of(new FilterItem("rlevel", EFilterRelation.gt, 0)),
				RECOMMENDATION_ORDER_COLUMNS);
	}

	@Override
	public IDataQuery<News> queryContentBeans(final String category) {
		final NewsCategory oCategory = _newsCategoryService.getBeanByName(category);
		if (oCategory == null) {
			return null;
		}
		return queryBeans(oCategory, null, new ColumnData[0]);
	}

	@Override
	public IDataQuery<News> queryContentBeans(final ID userId, final NewsCategory category,
			final EContentStatus status) {
		return queryBeans(category, status, null, FilterItems.of("userId", userId), ORDER_CREATEDATE);
	}

	private NewsLuceneService luceneService;

	@Override
	public ILuceneManager getLuceneService() {
		return luceneService;
	}

	@Override
	public void onInit() throws Exception {
		super.onInit();

		luceneService = new NewsLuceneService();
		if (!luceneService.indexExists()) {
			getModuleContext().getTaskExecutor().execute(new ExecutorRunnable() {
				@Override
				protected void task(final Map<String, Object> cache) throws Exception {
					getLog().info($m("NewsService.0"));
					luceneService.rebuildIndex();
					getLog().info($m("NewsService.1"));
				}
			});
		}

		addListener(new DbEntityAdapterEx<News>() {
			@Override
			public void onBeforeDelete(final IDbEntityManager<News> manager,
					final IParamsValue paramsValue) throws Exception {
				super.onBeforeDelete(manager, paramsValue);
				coll(manager, paramsValue); // 删除前缓存
			}

			@Override
			public void onAfterDelete(final IDbEntityManager<News> manager,
					final IParamsValue paramsValue) throws Exception {
				super.onAfterDelete(manager, paramsValue);
				for (final News news : coll(manager, paramsValue)) {
					final ID id = news.getId();
					// 删除附件
					newsContext.getAttachmentService().deleteWith("contentid=?", id);
					// 删除评论
					_newsCommentService.deleteWith("contentid=?", id);
					// 删除推荐
					_newsRecommendService.deleteWith("newsid=?", id);
					// 删除统计
					_newsStatService.deleteWith("categoryid=?", id);

					// 更新状态
					updateStats(news);

					// 删除索引
					if (news.isIndexed()) {
						luceneService.doDeleteIndex(news);
					}
				}
			}

			@Override
			public void onAfterInsert(final IDbEntityManager<News> service, final News[] beans)
					throws Exception {
				super.onAfterInsert(service, beans);
				for (final News news : beans) {
					// 更新状态
					updateStats(news);

					if (news.isIndexed()) {
						// 添加索引
						luceneService.doAddIndex(news);
					}
				}
			}

			@Override
			public void onBeforeUpdate(final IDbEntityManager<News> manager, final String[] columns,
					final News[] beans) throws Exception {
				super.onBeforeUpdate(manager, columns, beans);
				if (ArrayUtils.isEmpty(columns) || ArrayUtils.contains(columns, "categoryId", true)) {
					for (final News news : beans) {
						final Object categoryId = queryFor("categoryId", "id=?", news.getId());
						final NewsCategory category = _newsCategoryService.getBean(categoryId);
						ID _categoryId;
						if (category != null
								&& !(_categoryId = category.getId()).equals(news.getCategoryId())) {
							news.setAttr("_categoryId", _categoryId);
						}
					}
				}
			}

			@Override
			public void onAfterUpdate(final IDbEntityManager<News> service, final String[] columns,
					final News[] beans) throws Exception {
				super.onAfterUpdate(service, columns, beans);
				// 更新状态
				if (ArrayUtils.isEmpty(columns) || ArrayUtils.contains(columns, "status", true)
						|| ArrayUtils.contains(columns, "categoryId", true)) {
					for (final News news : beans) {
						updateStats(news);

						final ID _categoryId = (ID) news.getAttr("_categoryId");
						if (_categoryId != null) {
							updateStats(_categoryId, news.getDomainId());
						}
					}
				}

				// 更新索引
				if (ArrayUtils.isEmpty(columns) || ArrayUtils.contains(columns, "keyWords", true)
						|| ArrayUtils.contains(columns, "topic", true)
						|| ArrayUtils.contains(columns, "content", true)) {
					for (final News news : beans) {
						if (news.isIndexed()) {
							luceneService.doUpdateIndex(news);
						}
					}
				}
			}
		});
	}

	void updateStats(final News news) {
		updateStats(news.getCategoryId(), news.getDomainId());
	}

	void updateStats(final ID categoryId, final ID domainId) {
		final NewsStatService _newsStatServiceImpl = (NewsStatService) _newsStatService;
		final NewsStat stat = _newsStatService.getNewsStat(categoryId, domainId);
		_newsStatServiceImpl.reset(stat);
		_newsStatServiceImpl.setNewsStat(stat);
		_newsStatService.update(stat);
	}

	protected class NewsLuceneService extends AbstractLuceneManager {
		public NewsLuceneService() {
			super(new File(newsContext.getTmpdir() + "index"));
		}

		@Override
		protected String[] getQueryFields() {
			return new String[] { "id", "keyWord", "topic", "content" };
		}

		@Override
		protected Object documentToObject(final LuceneDocument doc, final Class<?> beanClass) {
			Object obj;
			if (beanClass == null) {
				obj = super.documentToObject(doc, beanClass);
			} else {
				obj = getBean(doc.get("id"));
			}
			return (obj != null && BeanUtils.getProperty(obj, "status") == EContentStatus.publish)
					? obj : null;
		}

		@Override
		protected IDataQuery<?> queryAll() {
			return getEntityManager().queryBeans(new ExpressionValue("indexed=?", true));
		}

		@Override
		protected void objectToDocument(final Object object, final LuceneDocument doc)
				throws IOException {
			super.objectToDocument(object, doc);
			final News news = (News) object;
			doc.addStringFields("keyWord", StringUtils.split(news.getKeyWords(), " "), false);
			doc.addTextField("topic", news.getTopic(), false);
			String content = news.getDescription();
			if (!StringUtils.hasText(content)) {
				content = trimContent(news.getContent());
			}
			doc.addTextField("content", content, false);
		}
	}
}
