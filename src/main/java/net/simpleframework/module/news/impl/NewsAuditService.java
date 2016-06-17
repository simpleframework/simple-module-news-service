package net.simpleframework.module.news.impl;

import net.simpleframework.ado.query.DataQueryUtils;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.ID;
import net.simpleframework.module.common.content.AbstractContentBean.EAuditStatus;
import net.simpleframework.module.common.content.AbstractContentBean.EContentStatus;
import net.simpleframework.module.common.log.LdescVal;
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
	public NewsAudit addNewsAudit(final News news, final ID userId, final boolean apass,
			final String ccomment) {
		final NewsAudit audit = new NewsAudit();
		audit.setNewsId(news.getId());
		audit.setUserId(userId);
		audit.setApass(apass);
		audit.setCcomment(ccomment);
		insert(audit);

		if (apass) {
			news.setAstatus(EAuditStatus.success);
			news.setStatus(EContentStatus.publish);
		} else {
			news.setAstatus(EAuditStatus.fail);
			news.setStatus(EContentStatus.edit);
			// LdescVal.set(bean, description);
		}
		_newsService.update(new String[] { "status", "astatus" }, news);
		return audit;
	}

	@Override
	public IDataQuery<NewsAudit> queryAudits(final News news) {
		if (news == null) {
			return DataQueryUtils.nullQuery();
		}
		return query("newsid=?", news.getId());
	}
}