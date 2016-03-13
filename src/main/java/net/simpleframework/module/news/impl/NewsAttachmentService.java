package net.simpleframework.module.news.impl;

import net.simpleframework.ctx.IModuleContext;
import net.simpleframework.module.news.NewsAttachment;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsAttachmentService extends AbstractNewsAttachmentService<NewsAttachment> {

	@Override
	public IModuleContext getModuleContext() {
		return newsContext;
	}
}