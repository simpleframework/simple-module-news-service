package net.simpleframework.module.news.impl;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.task.ExecutorRunnableEx;
import net.simpleframework.ctx.task.ITaskExecutor;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.common.content.ContentException;
import net.simpleframework.module.news.INewsContext;
import net.simpleframework.module.news.INewsRecommendService;
import net.simpleframework.module.news.News;
import net.simpleframework.module.news.NewsRecommend;
import net.simpleframework.module.news.NewsRecommend.ERecommendStatus;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsRecommendService extends AbstractNewsService<NewsRecommend> implements
		INewsRecommendService {

	@Override
	public IDataQuery<NewsRecommend> queryRecommends(final News news) {
		return query("newsid=?", news.getId());
	}

	@Override
	public NewsRecommend queryRunningRecommend(final News news) {
		return getBean("newsid=? and status=?", news.getId(), ERecommendStatus.running);
	}

	@Override
	public void doAbort(final NewsRecommend recommend) {
	}

	void _doCheck() {
	}

	@Transaction(context = INewsContext.class)
	public void doRecommend_inTran(final NewsRecommend r) {
	}

	@Override
	public void onInit() throws Exception {
		super.onInit();

		final ITaskExecutor taskExecutor = getTaskExecutor();
		taskExecutor.addScheduledTask(new ExecutorRunnableEx("newsrecommend_check") {
			@Override
			protected void task(final Map<String, Object> cache) throws Exception {
				_doCheck();
			}
		});

		addListener(new DbEntityAdapterEx<NewsRecommend>() {
			@Override
			public void onBeforeDelete(final IDbEntityManager<NewsRecommend> manager,
					final IParamsValue paramsValue) throws Exception {
				super.onBeforeDelete(manager, paramsValue);

				for (final NewsRecommend r : coll(manager, paramsValue)) {
					if (r.getStatus() == ERecommendStatus.running) {
						throw ContentException.of($m("NewsRecommendService.0"));
					}
				}
			}
		});
	}
}
