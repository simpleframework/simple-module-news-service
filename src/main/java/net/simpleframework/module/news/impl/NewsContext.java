package net.simpleframework.module.news.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.db.DbEntityTable;
import net.simpleframework.ctx.IModuleRef;
import net.simpleframework.ctx.Module;
import net.simpleframework.module.common.AbstractCommonModuleContext;
import net.simpleframework.module.common.content.Attachment;
import net.simpleframework.module.common.content.AttachmentLob;
import net.simpleframework.module.common.content.IAttachmentService;
import net.simpleframework.module.news.INewsCategoryService;
import net.simpleframework.module.news.INewsCommentService;
import net.simpleframework.module.news.INewsContext;
import net.simpleframework.module.news.INewsService;
import net.simpleframework.module.news.News;
import net.simpleframework.module.news.NewsCategory;
import net.simpleframework.module.news.NewsComment;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class NewsContext extends AbstractCommonModuleContext implements INewsContext {

	@Override
	protected DbEntityTable[] getEntityTables() {
		return new DbEntityTable[] { Attachment.TBL, AttachmentLob.TBL, NewsComment.TBL,
				NewsCategory.TBL, News.TBL };
	}

	@Override
	public String getManagerRole() {
		return ROLE_NEWS_MANAGER;
	}

	@Override
	protected Module createModule() {
		return new Module().setName(MODULE_NAME).setText($m("NewsContext.0")).setOrder(32);
	}

	@Override
	public IAttachmentService<Attachment> getAttachmentService() {
		return singleton(NewsAttachmentService.class);
	}

	@Override
	public INewsCommentService getCommentService() {
		return singleton(NewsCommentService.class);
	}

	@Override
	public INewsCategoryService getNewsCategoryService() {
		return singleton(NewsCategoryService.class);
	}

	@Override
	public INewsService getNewsService() {
		return singleton(NewsService.class);
	}

	@Override
	public IModuleRef getOrganizationRef() {
		return getRef("net.simpleframework.module.news.NewsOrganizationRef");
	}
}
