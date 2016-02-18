package net.simpleframework.module.news;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface INewsRecommendService extends IDbBeanService<NewsRecommend> {

	/**
	 * @param news
	 * 
	 * @return
	 */
	IDataQuery<NewsRecommend> queryRecommends(Object news);

	/**
	 * 获取正在运行的推荐
	 * 
	 * @param news
	 * @return
	 */
	NewsRecommend queryRunningRecommend(Object news);

	void doAbort(NewsRecommend recommend);
}
