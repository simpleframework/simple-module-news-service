package net.simpleframework.module.news.impl;

import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.module.common.content.Attachment;
import net.simpleframework.module.common.content.impl.AbstractAttachmentService;
import net.simpleframework.module.news.INewsContextAware;
import net.simpleframework.module.news.News;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractNewsAttachmentService<T extends Attachment> extends
		AbstractAttachmentService<T> implements INewsContextAware {

	@Override
	public void onInit() throws Exception {
		super.onInit();

		addListener(new DbEntityAdapterEx<T>() {
			@Override
			public void onBeforeDelete(final IDbEntityManager<T> manager,
					final IParamsValue paramsValue) throws Exception {
				super.onBeforeDelete(manager, paramsValue);
				for (final T attach : coll(manager, paramsValue)) {
					doVideoTime(attach, -attach.getVideoTime());
				}
			}

			@Override
			public void onAfterInsert(final IDbEntityManager<T> manager, final T[] beans)
					throws Exception {
				super.onAfterInsert(manager, beans);
				for (final T attach : beans) {
					doVideoTime(attach, 0);
				}
			}

			@Override
			public void onAfterUpdate(final IDbEntityManager<T> manager, final String[] columns,
					final T[] beans) throws Exception {
				super.onAfterUpdate(manager, columns, beans);
				if (ArrayUtils.isEmpty(columns) || ArrayUtils.contains(columns, "videoTime", true)) {
					for (final T attach : beans) {
						doVideoTime(attach, 0);
					}
				}
			}

			private void doVideoTime(final T attach, final int delta) {
				final News news = _newsService.getBean(attach.getContentId());
				news.setVideoTime(_newsService.sum("videoTime", "contentId=?", news.getId()).intValue()
						+ delta);
				_newsService.update(new String[] { "videoTime" }, news);
			}
		});
	}
}
