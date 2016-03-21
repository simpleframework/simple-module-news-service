package net.simpleframework.module.news.impl;

import java.util.Date;

import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.module.common.content.impl.AbstractCommentService;
import net.simpleframework.module.news.INewsCommentService;
import net.simpleframework.module.news.INewsContextAware;
import net.simpleframework.module.news.bean.News;
import net.simpleframework.module.news.bean.NewsComment;

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
		super.onInit();

		addListener(new DbEntityAdapterEx<NewsComment>() {

			@Override
			public void onBeforeDelete(final IDbEntityManager<NewsComment> manager,
					final IParamsValue paramsValue) throws Exception {
				super.onBeforeDelete(manager, paramsValue);

				// 修改统计值
				for (final NewsComment c : coll(manager, paramsValue)) {
					final News news = _newsService.getBean(c.getContentId());
					if (news != null) {
						news.setComments(queryComments(news).getCount() - 1);
						_newsService.update(new String[] { "comments" }, news);
					}
				}
			}

			@Override
			public void onAfterInsert(final IDbEntityManager<NewsComment> manager,
					final NewsComment[] beans) throws Exception {
				super.onAfterInsert(manager, beans);

				// 修改统计值
				for (final NewsComment c : beans) {
					final News news = _newsService.getBean(c.getContentId());
					if (news != null) {
						news.setComments(queryComments(news).getCount());
						news.setLastCommentDate(new Date());
						_newsService.update(new String[] { "comments", "lastCommentDate" }, news);
					}
				}
			}
		});
	}
}
