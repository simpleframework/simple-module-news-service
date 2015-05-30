package net.simpleframework.module.news.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.db.DbEntityTable;
import net.simpleframework.ado.db.IDbEntityTableRegistry;
import net.simpleframework.ctx.IApplicationContext;
import net.simpleframework.ctx.IModuleRef;
import net.simpleframework.ctx.Module;
import net.simpleframework.ctx.task.ExecutorRunnable;
import net.simpleframework.module.common.AbstractCommonModuleContext;
import net.simpleframework.module.common.content.Attachment;
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
public abstract class NewsContext extends AbstractCommonModuleContext implements INewsContext,
		IDbEntityTableRegistry {
	public static String ROLE_NEWS_MANAGER;

	@Override
	public void onInit(final IApplicationContext application) throws Exception {
		super.onInit(application);

		getTaskExecutor().addScheduledTask(60 * 10, new ExecutorRunnable() {
			@Override
			protected void task() throws Exception {
				getNewsService().doUnRecommendationTask();
			}
		});
	}

	@Override
	public DbEntityTable[] createEntityTables() {
		return new DbEntityTable[] { new DbEntityTable(NewsComment.class, "sf_news_comment"),
				new DbEntityTable(NewsCategory.class, "sf_news_category"),
				new DbEntityTable(News.class, "sf_news") };
	}

	@Override
	protected Module createModule() {
		return new Module() {
			@Override
			public String getManagerRole() {
				return ROLE_NEWS_MANAGER;
			}
		}.setName(MODULE_NAME).setText($m("NewsContext.0")).setOrder(32);
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
