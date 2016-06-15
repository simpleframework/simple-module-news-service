package net.simpleframework.module.news.bean;

import net.simpleframework.ado.bean.AbstractUserAwareBean;
import net.simpleframework.ado.db.common.EntityInterceptor;
import net.simpleframework.common.ID;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@EntityInterceptor(listenerTypes = { "net.simpleframework.module.log.EntityDeleteLogAdapter" })
public class NewsAudit extends AbstractUserAwareBean {
	/* 新闻id */
	private ID newsId;
	/* 审核通过 */
	private boolean apass;

	/* 内容 */
	private String ccomment;

	public ID getNewsId() {
		return newsId;
	}

	public void setNewsId(final ID newsId) {
		this.newsId = newsId;
	}

	public boolean isApass() {
		return apass;
	}

	public void setApass(final boolean apass) {
		this.apass = apass;
	}

	public String getCcomment() {
		return ccomment;
	}

	public void setCcomment(final String ccomment) {
		this.ccomment = ccomment;
	}

	private static final long serialVersionUID = 1390020297704889436L;
}
