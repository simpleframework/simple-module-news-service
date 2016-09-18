package net.simpleframework.module.news.impl;

import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.common.MimeTypes;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.ctx.common.bean.AttachmentFile;
import net.simpleframework.lib.it.sauronsoftware.jave.Encoder;
import net.simpleframework.lib.it.sauronsoftware.jave.MultimediaInfo;
import net.simpleframework.module.common.content.impl.AbstractAttachmentService;
import net.simpleframework.module.news.INewsContextAware;
import net.simpleframework.module.news.bean.News;
import net.simpleframework.module.news.bean.NewsAttachment;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsAttachmentService extends AbstractAttachmentService<NewsAttachment>
		implements INewsContextAware {

	@Override
	public void onInit() throws Exception {
		super.onInit();

		addListener(new _NewsAttachmentAdapter());
	}

	@Override
	public long getAttachsize(final Object user) {
		return sum("attachsize", "userid=? and imodule=?", getIdParam(user), 11).longValue();
	}

	public static class _NewsAttachmentAdapter extends DbEntityAdapterEx<NewsAttachment> {

		@Override
		public void onBeforeDelete(final IDbEntityManager<NewsAttachment> manager,
				final IParamsValue paramsValue) throws Exception {
			super.onBeforeDelete(manager, paramsValue);
			for (final NewsAttachment attach : coll(manager, paramsValue)) {
				doVideoTime(attach, -attach.getVideoTime());
			}
		}

		@Override
		public void onBeforeInsert(final IDbEntityManager<NewsAttachment> manager,
				final NewsAttachment[] beans) throws Exception {
			super.onBeforeInsert(manager, beans);
			for (final NewsAttachment attach : beans) {
				// 设置附件类型
				attach.setAttachtype(newsContext.getAttachmentType(attach));

				// 设置视频时长
				final AttachmentFile af = (AttachmentFile) attach.getAttr("_AttachmentFile");
				if (af != null) {
					final String ext = attach.getFileExt();
					if (StringUtils.hasText(ext)) {
						if (MimeTypes.getMimeType(ext).startsWith("video/")) {
							final Encoder encoder = new Encoder();
							final MultimediaInfo info = encoder.getInfo(af.getAttachment());
							final int duration = (int) (info.getDuration() / 1000);
							attach.setVideoTime(duration);
						}
					}
				}
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
			// 统计视频时间
			final News news = _newsService.getBean(attach.getContentId());
			if (news != null) {
				news.setVideoTime(
						_newsAttachService.sum("videoTime", "contentId=?", news.getId()).intValue()
								+ delta);
				_newsService.update(new String[] { "videoTime" }, news);
			}
		}
	};
}