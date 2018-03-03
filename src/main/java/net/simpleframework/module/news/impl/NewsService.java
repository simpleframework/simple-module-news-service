package net.simpleframework.module.news.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.EFilterOpe;
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
import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.TimePeriod;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.ctx.task.ExecutorRunnableEx;
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
	public News createBean(final ID userId, final boolean assertTimeInterval) {
		if (assertTimeInterval) {
			assertTimeInterval(new ExpressionValue("userid=?", userId), 60);
		}
		final News news = super.createBean();
		news.setUserId(userId);
		return news;
	}

	@Override
	public IDataQuery<News> queryBeans(final AbstractCategoryBean oCategory, final String domainId,
			final String nclass, final EContentStatus status, final TimePeriod timePeriod,
			FilterItems filterItems, final ColumnData... orderColumns) {
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

		if (StringUtils.hasText(nclass)) {
			String[] arr = StringUtils.split(nclass, ";");
			if (arr.length >= 2) {
				arr = ArrayUtils.add(arr, nclass);
			}
			int i = 0;
			for (final String s : arr) {
				final FilterItem item = new FilterItem("nclass", s);
				if (i++ == 0) {
					item.setLbracket(true);
				} else {
					item.setOpe(EFilterOpe.or);
				}
				if (i == arr.length) {
					item.setRbracket(true);
				}
				filterItems.add(item);
			}
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
		return queryBeans(oCategory, null, null, status, timePeriod, filterItems, orderColumns);
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

	protected boolean isIndexed(final News news) {
		return news.isIndexed();
	}

	@Override
	public void onInit() throws Exception {
		super.onInit();

		luceneService = new NewsLuceneService();
		getModuleContext().getTaskExecutor().execute(new ExecutorRunnableEx("lucene_startup_news") {
			@Override
			protected void task(final Map<String, Object> cache) throws Exception {
				if (!luceneService.indexExists()) {
					oprintln("Rebuild news index...");
					luceneService.rebuildIndex();
					oprintln("Rebuild news index end.");
				} else {
					oprintln("Optimize news index...");
					luceneService.optimize();
					oprintln("Optimize news index end.");
				}
			}
		});

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

					try {
						// 删除索引
						if (isIndexed(news)) {
							luceneService.doDeleteIndex(news);
						}
					} catch (final Exception e) {
						log.warn(e);
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

					try {
						if (isIndexed(news)) {
							// 添加索引
							luceneService.doAddIndex(news);
						}
					} catch (final Exception e) {
						log.warn(e);
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
				if (ArrayUtils.isEmpty(columns) || ArrayUtils.contains(columns, "status", true)
						|| ArrayUtils.contains(columns, "keyWords", true)
						|| ArrayUtils.contains(columns, "topic", true)
						|| ArrayUtils.contains(columns, "description", true)
						|| ArrayUtils.contains(columns, "content", true)) {
					for (final News news : beans) {
						try {
							if (isIndexed(news)) {
								luceneService.doUpdateIndex(news);
							}
						} catch (final Exception e) {
							log.warn(e);
						}
					}
				}
			}
		});
	}

	void updateStats(final News news) {
		updateStats(news.getCategoryId(), news.getDomainId());
	}

	void updateStats(final ID categoryId, final String domainId) {
		final NewsStatService _newsStatServiceImpl = (NewsStatService) _newsStatService;
		final NewsStat stat = _newsStatService.getNewsStat(categoryId, domainId);
		_newsStatServiceImpl.reset(stat);
		_newsStatServiceImpl.setNewsStat(stat);
		_newsStatService.update(stat);
	}

	protected File getIndexDir() {
		return getApplicationContext().getContextSettings().getHomeFile("/index/news/");
	}

	protected class NewsLuceneService extends AbstractLuceneManager {
		public NewsLuceneService() {
			super(getIndexDir());
		}

		@Override
		protected String[] getQueryFields() {
			return new String[] { "keyWord", "topic", "content" };
		}

		@Override
		protected Query getQuery(final String domain, final String[] queryFields,
				final String queryString) {
			final Query query = super.getQuery(domain, queryFields, queryString);
			if (query == null) {
				return null;
			}
			final BooleanQuery.Builder builder = new BooleanQuery.Builder();
			if (StringUtils.hasText(domain)) {
				builder.add(new TermQuery(new Term("userid", domain)), Occur.MUST);
			}
			builder.add(new TermQuery(new Term("status", "publish")), Occur.MUST);
			builder.add(query, Occur.MUST);
			return builder.build();
		}

		@Override
		protected Object documentToObject(final LuceneDocument doc, final Class<?> bClass) {
			return bClass != null ? getBean(doc.get("id")) : super.documentToObject(doc, bClass);
		}

		@Override
		protected void objectToDocument(final Object object, final LuceneDocument doc)
				throws IOException {
			super.objectToDocument(object, doc);
			final News news = (News) object;
			doc.addStringField("userid", String.valueOf(news.getUserId()), false);
			doc.addStringField("status", news.getStatus().name(), false);
			doc.addStringFields("keyWord", StringUtils.split(news.getKeyWords(), " "), false);
			doc.addTextField("topic", news.getTopic(), false);
			String content = news.getDescription();
			if (!StringUtils.hasText(content)) {
				content = trimContent(news.getContent());
			}
			doc.addTextField("content", content, false);
		}

		@Override
		protected IDataQuery<?> queryAll() {
			return getEntityManager().queryBeans(new ExpressionValue("indexed=?", true));
		}
	}
}
