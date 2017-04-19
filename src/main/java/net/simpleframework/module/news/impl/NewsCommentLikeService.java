package net.simpleframework.module.news.impl;

import net.simpleframework.ctx.IModuleContext;
import net.simpleframework.module.common.content.impl.AbstractLikeService;
import net.simpleframework.module.news.INewsCommentLikeService;
import net.simpleframework.module.news.INewsContextAware;
import net.simpleframework.module.news.bean.NewsComment;
import net.simpleframework.module.news.bean.NewsCommentLike;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsCommentLikeService extends AbstractLikeService<NewsComment, NewsCommentLike>
		implements INewsCommentLikeService, INewsContextAware {

	@Override
	public IModuleContext getModuleContext() {
		return newsContext;
	}

	@Override
	protected void setLike(final NewsCommentLike like, final NewsComment comment) {
		like.setCommentId(comment.getId());
		like.setNewsId(comment.getContentId());
	}

	@Override
	protected void updateStats(final NewsCommentLike like, final Number i) {
		final NewsComment nc = _newsCommentService.getBean(like.getCommentId());
		final int c = count("commentid=?", nc.getId());
		nc.setLikes(c + i.intValue());
		_newsCommentService.update(new String[] { "likes" }, nc);
	}
}