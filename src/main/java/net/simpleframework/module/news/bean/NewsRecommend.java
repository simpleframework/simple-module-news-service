package net.simpleframework.module.news.bean;

import net.simpleframework.ado.db.common.EntityInterceptor;
import net.simpleframework.common.ID;
import net.simpleframework.module.common.content.AbstractRecommend;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@EntityInterceptor(listenerTypes = { "net.simpleframework.module.log.EntityDeleteLogAdapter" })
public class NewsRecommend extends AbstractRecommend {
	/* 新闻id */
	private ID newsId;

	public ID getNewsId() {
		return newsId;
	}

	public void setNewsId(final ID newsId) {
		this.newsId = newsId;
	}

	private static final long serialVersionUID = 6285609326593701390L;
}
