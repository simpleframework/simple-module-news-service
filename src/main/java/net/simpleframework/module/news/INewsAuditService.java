package net.simpleframework.module.news;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.ID;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;
import net.simpleframework.module.news.bean.News;
import net.simpleframework.module.news.bean.NewsAudit;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface INewsAuditService extends IDbBeanService<NewsAudit> {

	/**
	 * 添加审核记录
	 * 
	 * @param news
	 * @param userId
	 * @param apass
	 * @param ccomment
	 * @return
	 */
	NewsAudit addNewsAudit(News news, ID userId, boolean apass, String ccomment);

	/**
	 * 查询新闻的审核记录
	 * 
	 * @param news
	 * @return
	 */
	IDataQuery<NewsAudit> queryAudits(News news);
}
