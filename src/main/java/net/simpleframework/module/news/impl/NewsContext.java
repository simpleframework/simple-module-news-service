package net.simpleframework.module.news.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.DbEntityTable;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.ctx.IApplicationContext;
import net.simpleframework.ctx.IModuleRef;
import net.simpleframework.ctx.Module;
import net.simpleframework.ctx.ModuleRefUtils;
import net.simpleframework.ctx.service.ado.db.AbstractDbBeanService.DbEntityAdapterEx;
import net.simpleframework.module.common.AbstractCommonModuleContext;
import net.simpleframework.module.common.content.IAttachmentService;
import net.simpleframework.module.news.INewsCategoryService;
import net.simpleframework.module.news.INewsCommentService;
import net.simpleframework.module.news.INewsContext;
import net.simpleframework.module.news.INewsRecommendService;
import net.simpleframework.module.news.INewsService;
import net.simpleframework.module.news.INewsStatService;
import net.simpleframework.module.news.News;
import net.simpleframework.module.news.NewsAttachment;
import net.simpleframework.module.news.NewsCategory;
import net.simpleframework.module.news.NewsComment;
import net.simpleframework.module.news.NewsRecommend;
import net.simpleframework.module.news.NewsStat;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class NewsContext extends AbstractCommonModuleContext implements INewsContext {

	@Override
	protected DbEntityTable[] createEntityTables() {
		return new DbEntityTable[] { new DbEntityTable(NewsCategory.class, "sf_news_category"),
				new DbEntityTable(News.class, "sf_news"),
				new DbEntityTable(NewsComment.class, "sf_news_comment"),
				new DbEntityTable(NewsRecommend.class, "sf_news_recommend"),
				new DbEntityTable(NewsStat.class, "sf_news_stat"),
				new DbEntityTable(NewsAttachment.class, "sf_attachment"), SF_ATTACHMENT_LOB };
	}

	@Override
	protected Module createModule() {
		return new Module().setName(MODULE_NAME).setText($m("NewsContext.0")).setOrder(32);
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
	public INewsRecommendService getNewsRecommendService() {
		return singleton(NewsRecommendService.class);
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

	@Override
	public void onInit(final IApplicationContext application) throws Exception {
		super.onInit(application);

		final IAttachmentService<NewsAttachment> attachService = getAttachmentService();
		attachService.addListener(new DbEntityAdapterEx<NewsAttachment>() {
			@Override
			public void onBeforeDelete(final IDbEntityManager<NewsAttachment> manager,
					final IParamsValue paramsValue) throws Exception {
				super.onBeforeDelete(manager, paramsValue);
				for (final NewsAttachment attach : coll(manager, paramsValue)) {
					doVideoTime(attach, -attach.getVideoTime());
				}
			}

			@Override
			public void onAfterInsert(final IDbEntityManager<NewsAttachment> manager,
					final NewsAttachment[] beans) throws Exception {
				super.onAfterInsert(manager, beans);
				for (final NewsAttachment attach : beans) {
					doVideoTime(attach, 0);
				}
			}

			@Override
			public void onAfterUpdate(final IDbEntityManager<NewsAttachment> manager,
					final String[] columns, final NewsAttachment[] beans) throws Exception {
				super.onAfterUpdate(manager, columns, beans);
				if (ArrayUtils.isEmpty(columns) || ArrayUtils.contains(columns, "videoTime", true)) {
					for (final NewsAttachment attach : beans) {
						doVideoTime(attach, 0);
					}
				}
			}

			private void doVideoTime(final NewsAttachment attach, final int delta) {
				final INewsService newsService = getNewsService();
				final News news = newsService.getBean(attach.getContentId());
				news.setVideoTime(attachService.sum("videoTime", "contentId=?", news.getId())
						.intValue() + delta);
				newsService.update(new String[] { "videoTime" }, news);
			}
		});
	}
}
