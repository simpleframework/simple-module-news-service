package net.simpleframework.module.news.impl;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.common.content.AbstractRecommend.ERecommendStatus;
import net.simpleframework.module.common.content.impl.AbstractRecommendService;
import net.simpleframework.module.news.INewsContextAware;
import net.simpleframework.module.news.INewsRecommendService;
import net.simpleframework.module.news.bean.News;
import net.simpleframework.module.news.bean.NewsRecommend;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsRecommendService extends AbstractRecommendService<NewsRecommend>
		implements INewsRecommendService, INewsContextAware {

	@Override
	public IDataQuery<NewsRecommend> queryRecommends(final Object news) {
		return query("newsid=? order by createdate desc", getIdParam(news));
	}

	@Override
	public NewsRecommend queryRunningRecommend(final Object news) {
		return getBean("newsid=? and status=?", getIdParam(news), ERecommendStatus.running);
	}

	@Override
	protected Object getContent(final NewsRecommend t) {
		return t.getNewsId();
	}

	@Override
	protected void _doStatus(final NewsRecommend r, final ERecommendStatus status) {
		r.setStatus(status);
		update(new String[] { "status" }, r);

		final News news = _newsService.getBean(r.getNewsId());
		news.setRlevel(status == ERecommendStatus.running ? r.getRlevel() : 0);
		_newsService.update(new String[] { "rlevel" }, news);
	}
}
