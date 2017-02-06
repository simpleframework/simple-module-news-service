package net.simpleframework.module.news.impl;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.ado.db.DbEntityTable;
import net.simpleframework.ctx.IModuleRef;
import net.simpleframework.ctx.Module;
import net.simpleframework.ctx.ModuleRefUtils;
import net.simpleframework.module.common.AbstractCommonModuleContext;
import net.simpleframework.module.common.content.Attachment;
import net.simpleframework.module.common.content.IAttachmentService;
import net.simpleframework.module.news.INewsAuditService;
import net.simpleframework.module.news.INewsCategoryService;
import net.simpleframework.module.news.INewsCommentLikeService;
import net.simpleframework.module.news.INewsCommentService;
import net.simpleframework.module.news.INewsContext;
import net.simpleframework.module.news.INewsRecommendService;
import net.simpleframework.module.news.INewsService;
import net.simpleframework.module.news.INewsStatService;
import net.simpleframework.module.news.bean.News;
import net.simpleframework.module.news.bean.NewsAttachment;
import net.simpleframework.module.news.bean.NewsAudit;
import net.simpleframework.module.news.bean.NewsCategory;
import net.simpleframework.module.news.bean.NewsComment;
import net.simpleframework.module.news.bean.NewsCommentLike;
import net.simpleframework.module.news.bean.NewsRecommend;
import net.simpleframework.module.news.bean.NewsStat;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsContext extends AbstractCommonModuleContext implements INewsContext {

	@Override
	protected DbEntityTable[] createEntityTables() {
		return new DbEntityTable[] { new DbEntityTable(NewsCategory.class, "sf_news_category"),
				new DbEntityTable(News.class, "sf_news"),

				new DbEntityTable(NewsStat.class, "sf_news_stat"),

				new DbEntityTable(NewsComment.class, "sf_news_comment"),
				new DbEntityTable(NewsCommentLike.class, "sf_news_comment_like"),

				new DbEntityTable(NewsRecommend.class, "sf_news_recommend"),

				new DbEntityTable(NewsAudit.class, "sf_news_audit"),

				new DbEntityTable(NewsAttachment.class, "sf_attachment"), SF_ATTACHMENT_LOB };
	}

	@Override
	protected Module createModule() {
		return super.createModule().setName(MODULE_NAME).setText($m("NewsContext.0")).setOrder(32);
	}

	@Override
	public int getAttachmentType(final Attachment attach) {
		return attach.getAttachtype();
	}

	@Override
	public INewsService getNewsService() {
		return singleton(NewsService.class);
	}

	@Override
	public INewsCategoryService getNewsCategoryService() {
		return singleton(NewsCategoryService.class);
	}

	@Override
	public INewsCommentService getCommentService() {
		return singleton(NewsCommentService.class);
	}

	@Override
	public INewsCommentLikeService getNewsCommentLikeService() {
		return singleton(NewsCommentLikeService.class);
	}

	@Override
	public INewsRecommendService getNewsRecommendService() {
		return singleton(NewsRecommendService.class);
	}

	@Override
	public INewsAuditService getNewsAuditService() {
		return singleton(NewsAuditService.class);
	}

	@Override
	public INewsStatService getNewsStatService() {
		return singleton(NewsStatService.class);
	}

	@Override
	public IAttachmentService<NewsAttachment> getAttachmentService() {
		return singleton(NewsAttachmentService.class);
	}

	@Override
	public IModuleRef getOrganizationRef() {
		return ModuleRefUtils.getRef("net.simpleframework.module.news.NewsOrganizationRef");
	}
}
