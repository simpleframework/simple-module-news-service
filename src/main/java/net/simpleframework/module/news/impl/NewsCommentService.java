package net.simpleframework.module.news.impl;

import java.util.Date;

import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.module.common.content.impl.AbstractCommentService;
import net.simpleframework.module.news.INewsCommentService;
import net.simpleframework.module.news.INewsContextAware;
import net.simpleframework.module.news.INewsService;
import net.simpleframework.module.news.News;
import net.simpleframework.module.news.NewsComment;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsCommentService extends AbstractCommentService<NewsComment> implements
		INewsCommentService, INewsContextAware {

	@Override
	public void onInit() throws Exception {
		addListener(new DbEntityAdapterEx() {

			@Override
			public void onBeforeDelete(final IDbEntityManager<?> manager,
					final IParamsValue paramsValue) {
				super.onBeforeDelete(manager, paramsValue);

				// 修改统计值
				final INewsService nService = newsContext.getNewsService();
				for (final NewsComment c : coll(paramsValue)) {
					final News news = nService.getBean(c.getContentId());
					if (news != null) {
						news.setComments(queryComments(news).getCount() - 1);
						nService.update(new String[] { "comments" }, news);
					}
				}
			}

			@Override
			public void onAfterInsert(final IDbEntityManager<?> manager, final Object[] beans) {
				super.onAfterInsert(manager, beans);

				// 修改统计值
				final INewsService nService = newsContext.getNewsService();
				for (final Object o : beans) {
					final NewsComment c = (NewsComment) o;
					final News news = nService.getBean(c.getContentId());
					if (news != null) {
						news.setComments(queryComments(news).getCount());
						news.setLastCommentDate(new Date());
						nService.update(new String[] { "comments", "lastCommentDate" }, news);
					}
				}
			}
		});
	}
}
