package net.simpleframework.module.news;

import net.simpleframework.ctx.IModuleRef;
import net.simpleframework.module.common.ICommonModuleContext;
import net.simpleframework.module.common.content.IAttachmentService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface INewsContext extends ICommonModuleContext {

	static final String MODULE_NAME = "simple-module-news";

	/**
	 * 获取新闻类目管理器
	 * 
	 * @return
	 */
	INewsCategoryService getNewsCategoryService();

	/**
	 * 获取新闻管理器
	 * 
	 * @return
	 */
	INewsService getNewsService();

	/**
	 * 获取评论管理器
	 * 
	 * @return
	 */
	INewsCommentService getCommentService();

	/**
	 * 获取统计服务
	 * 
	 * @return
	 */
	INewsStatService getNewsStatService();

	@Override
	IAttachmentService<NewsAttachment> getAttachmentService();

	/**
	 * 获取机构的引用
	 * 
	 * @return
	 */
	IModuleRef getOrganizationRef();
}
