package net.simpleframework.module.news;

import net.simpleframework.ctx.IModuleContextAware;
import net.simpleframework.ctx.ModuleContextFactory;
import net.simpleframework.module.common.content.IAttachmentService;
import net.simpleframework.module.news.bean.NewsAttachment;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface INewsContextAware extends IModuleContextAware {

	static INewsContext newsContext = ModuleContextFactory.get(INewsContext.class);

	/* 新闻服务 */
	static INewsService _newsService = newsContext.getNewsService();
	/* 新闻类目服务 */
	static INewsCategoryService _newsCategoryService = newsContext.getNewsCategoryService();

	/* 新闻评论服务 */
	static INewsCommentService _newsCommentService = newsContext.getCommentService();

	/* 新闻推荐服务 */
	static INewsRecommendService _newsRecommendService = newsContext.getNewsRecommendService();

	/* 附件服务 */
	static IAttachmentService<NewsAttachment> _newsAttachService = newsContext
			.getAttachmentService();

	/* 新闻统计信息服务 */
	static INewsStatService _newsStatService = newsContext.getNewsStatService();
}