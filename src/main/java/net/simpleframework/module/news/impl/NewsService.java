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
import net.simpleframework.ado.db.common.SQLValue;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.ado.lucene.ILuceneManager;
import net.simpleframework.ado.lucene.LuceneDocument;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.Convert;
import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.TimePeriod;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.ctx.task.ExecutorRunnable;
import net.simpleframework.module.common.content.AbstractCategoryBean;
import net.simpleframework.module.common.content.EContentStatus;
import net.simpleframework.module.common.content.impl.AbstractContentService;
import net.simpleframework.module.news.INewsContextAware;
import net.simpleframework.module.news.INewsService;
import net.simpleframework.module.news.News;
import net.simpleframework.module.news.NewsCategory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsService extends AbstractContentService<News> implements INewsService,
		INewsContextAware {

	@Override
	public IDataQuery<News> queryBeans(final AbstractCategoryBean oCategory,
			final EContentStatus status, final TimePeriod timePeriod, FilterItems filterItems,
			final ColumnData... orderColumns) {
		if (filterItems == null) {
			filterItems = FilterItems.of();
		}

		filterItems.addEqual("domain", getModuleContext().getDomain());
		if (oCategory != null) {
			filterItems.addEqual("categoryId", oCategory.getId());
		}
		if (timePeriod != null) {
			filterItems.addEqual("createdate", timePeriod);
		}

		if (status != null) {
			filterItems.add(new FilterItem("status", status));
		} else {
			filterItems
					.add(new FilterItem("status", EFilterRelation.not_equal, EContentStatus.delete));
		}

		return queryByParams(filterItems, orderColumns);
	}

	@Override
	public News getBeanByName(final String name) {
		return StringUtils.hasText(name) ? getBean("cname=?", name) : null;
	}

	@Override
	public IDataQuery<News> queryImageNews(final NewsCategory oCategory) {
		return queryBeans(oCategory, EContentStatus.publish, null, FilterItems.of("imageMark", true),
				new ColumnData[] { ColumnData.DESC("recommendation"), ColumnData.DESC("createdate") });
	}

	@Override
	public IDataQuery<News> queryContentBeans(final String category) {
		final NewsCategory oCategory = newsContext.getNewsCategoryService().getBeanByName(category);
		if (oCategory == null) {
			return null;
		}
		return queryBeans(oCategory, null, ColumnData.EMPTY);
	}

	@Override
	public int count(final NewsCategory category) {
		if (COUNT_STATS.size() == 0) {
			final IDataQuery<Map<String, Object>> dq = getQueryManager().query(
					new SQLValue("select categoryId, count(*) as cc from " + getTablename(News.class)
							+ " a where a.domain=? and a.status<>? group by categoryId",
							getModuleContext().getDomain(), EContentStatus.delete));
			for (Map<String, Object> row; (row = dq.next()) != null;) {
				COUNT_STATS.put(Convert.toString(row.get("categoryId")), Convert.toInt(row.get("cc")));
			}
		}
		return Convert.toInt(COUNT_STATS.get(category.getId().toString()));
	}

	@Override
	public ILuceneManager getLuceneService() {
		return luceneService;
	}

	NewsLuceneService luceneService;

	@Override
	public void onInit() throws Exception {
		super.onInit();

		luceneService = new NewsLuceneService(new File(newsContext.getTmpdir() + "index"));
		if (!luceneService.indexExists()) {
			getModuleContext().getTaskExecutor().execute(new ExecutorRunnable() {
				@Override
				protected void task() throws Exception {
					getLog().info($m("NewsService.0"));
					luceneService.rebuildIndex();
					getLog().info($m("NewsService.1"));
				}
			});
		}

		addListener(new DbEntityAdapterEx() {
			@Override
			public void onBeforeDelete(final IDbEntityManager<?> service,
					final IParamsValue paramsValue) {
				super.onBeforeDelete(service, paramsValue);
				coll(paramsValue); // 删除前缓存
			}

			@Override
			public void onAfterDelete(final IDbEntityManager<?> service, final IParamsValue paramsValue) {
				super.onAfterDelete(service, paramsValue);
				final NewsAttachmentService aService = (NewsAttachmentService) newsContext
						.getAttachmentService();
				final NewsCommentService cService = (NewsCommentService) newsContext
						.getCommentService();
				for (final News news : coll(paramsValue)) {
					final ID id = news.getId();
					aService.deleteWith("contentId=?", id);
					cService.deleteWith("contentId=?", id);
					// 删除索引
					if (news.isIndexed()) {
						luceneService.doDeleteIndex(news);
					}
				}
			}

			@Override
			public void onAfterInsert(final IDbEntityManager<?> service, final Object[] beans) {
				super.onAfterInsert(service, beans);

				for (final Object o : beans) {
					final News news = (News) o;
					if (news.isIndexed()) {
						// 添加索引
						luceneService.doAddIndex(news);
					}
				}
			}

			@Override
			public void onAfterUpdate(final IDbEntityManager<?> service, final String[] columns,
					final Object[] beans) {
				super.onAfterUpdate(service, columns, beans);

				// 更新索引
				if (ArrayUtils.isEmpty(columns) || ArrayUtils.contains(columns, "keyWords", true)
						|| ArrayUtils.contains(columns, "topic", true)
						|| ArrayUtils.contains(columns, "content", true)) {
					for (final Object o : beans) {
						final News news = (News) o;
						if (news.isIndexed()) {
							luceneService.doUpdateIndex(news);
						}
					}
				}
			}
		});
	}

	static class NewsLuceneService extends AbstractLuceneManager {
		public NewsLuceneService(final File indexPath) {
			super(indexPath, new String[] { "id", "keyWord", "topic", "content" });
		}

		@Override
		protected Object documentToObject(final LuceneDocument doc, final Class<?> beanClass) {
			final News news = newsContext.getNewsService().getBean(doc.get("id"));
			return news != null && news.getStatus() == EContentStatus.publish ? news : null;
		}

		@Override
		protected IDataQuery<?> queryAll() {
			return newsContext.getNewsService().getEntityManager()
					.queryBeans(new ExpressionValue("indexed=?", true));
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
