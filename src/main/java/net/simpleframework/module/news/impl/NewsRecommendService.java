package net.simpleframework.module.news.impl;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
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
		_doStatus(recommend, ERecommendStatus.abort);
	}

	void _doRecommendTask(final NewsRecommend r) {
		if (r.getStatus() == ERecommendStatus.ready) {
			final Date startDate = r.getDstartDate();
			final Date n = new Date();
			if (startDate == null || startDate.before(n)) {
				_doStatus(r, ERecommendStatus.running);
			}
		}
	}

	private void _doStatus(final NewsRecommend r, final ERecommendStatus status) {
		r.setStatus(status);
		update(new String[] { "status" }, r);

		final News news = _newsService.getBean(r.getNewsId());
		news.setRlevel(status == ERecommendStatus.running ? r.getRlevel() : 0);
		_newsService.update(new String[] { "rlevel" }, news);
	}

	@Transaction(context = INewsContext.class)
	public void doRecommend_inTran(final NewsRecommend r) {
		if (r.getStatus() == ERecommendStatus.ready) {
			_doRecommendTask(r);
		} else {
			final Date endDate = r.getDendDate();
			if (endDate != null && endDate.before(new Date())) {
				_doStatus(r, ERecommendStatus.complete);
			}
		}
	}

	@Override
	public void onInit() throws Exception {
		super.onInit();

		final ITaskExecutor taskExecutor = getTaskExecutor();
		taskExecutor.addScheduledTask(new ExecutorRunnableEx("newsrecommend_check") {
			@Override
			protected void task(final Map<String, Object> cache) throws Exception {
				final IDataQuery<NewsRecommend> dq = query("status=? or status=?",
						ERecommendStatus.ready, ERecommendStatus.running).setFetchSize(0);
				NewsRecommend r;
				while ((r = dq.next()) != null) {
					doRecommend_inTran(r);
				}
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

			@Override
			public void onAfterUpdate(final IDbEntityManager<NewsRecommend> manager,
					final String[] columns, final NewsRecommend[] beans) throws Exception {
				super.onAfterUpdate(manager, columns, beans);
				for (final NewsRecommend r : beans) {
					_doRecommendTask(r);
				}
			}
		});
	}
}
