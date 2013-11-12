package net.simpleframework.module.news;

import static net.simpleframework.ctx.permission.IPermissionConst.ROLECHART_SYSTEM;
import net.simpleframework.ctx.IModuleRef;
import net.simpleframework.ctx.permission.PermissionRole;
import net.simpleframework.module.common.ICommonModuleContext;
import net.simpleframework.module.common.content.Attachment;
import net.simpleframework.module.common.content.IAttachmentService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface INewsContext extends ICommonModuleContext {

	static final String ROLE_NEWS_MANAGER = PermissionRole.toUniqueRolename(ROLECHART_SYSTEM,
			"news_manager");

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

	@Override
	IAttachmentService<Attachment> getAttachmentService();

	/**
	 * 获取机构的引用
	 * 
	 * @return
	 */
	IModuleRef getOrganizationRef();
}
