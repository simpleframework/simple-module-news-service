package net.simpleframework.module.news;

import net.simpleframework.module.common.content.ILikeService;
import net.simpleframework.module.news.bean.NewsComment;
import net.simpleframework.module.news.bean.NewsCommentLike;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface INewsCommentLikeService extends ILikeService<NewsComment, NewsCommentLike> {
}
