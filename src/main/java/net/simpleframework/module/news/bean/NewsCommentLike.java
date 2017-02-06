package net.simpleframework.module.news.bean;

import net.simpleframework.ado.bean.AbstractUserAwareBean;
import net.simpleframework.common.ID;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsCommentLike extends AbstractUserAwareBean {
	/* 评论id */
	private ID commentId;

	/* 文档id */
	private ID newsId;

	public ID getCommentId() {
		return commentId;
	}

	public void setCommentId(final ID commentId) {
		this.commentId = commentId;
	}

	public ID getNewsId() {
		return newsId;
	}

	public void setNewsId(final ID newsId) {
		this.newsId = newsId;
	}

	private static final long serialVersionUID = 1704598006505952849L;
}
