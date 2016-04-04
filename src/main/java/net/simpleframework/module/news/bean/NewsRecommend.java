package net.simpleframework.module.news.bean;

import net.simpleframework.common.ID;
import net.simpleframework.module.common.bean.AbstractRecommend;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsRecommend extends AbstractRecommend {

	private ID newsId;

	public ID getNewsId() {
		return newsId;
	}

	public void setNewsId(final ID newsId) {
		this.newsId = newsId;
	}

	private static final long serialVersionUID = 6285609326593701390L;
}
