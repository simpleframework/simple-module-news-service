package net.simpleframework.module.news.impl;

import net.simpleframework.ado.query.DataQueryUtils;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.news.INewsAuditService;
import net.simpleframework.module.news.bean.News;
import net.simpleframework.module.news.bean.NewsAudit;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsAuditService extends AbstractNewsService<NewsAudit> implements INewsAuditService {

	@Override
	public IDataQuery<NewsAudit> queryAudits(final News news) {
		if (news == null) {
			return DataQueryUtils.nullQuery();
		}
		return query("newsid=?", news.getId());
	}
}