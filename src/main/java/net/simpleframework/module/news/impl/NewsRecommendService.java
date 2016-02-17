package net.simpleframework.module.news.impl;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.news.INewsRecommendService;
import net.simpleframework.module.news.News;
import net.simpleframework.module.news.NewsRecommend;

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
}
