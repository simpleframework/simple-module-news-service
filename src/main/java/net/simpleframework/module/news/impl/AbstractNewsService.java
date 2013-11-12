package net.simpleframework.module.news.impl;

import java.io.Serializable;

import net.simpleframework.ctx.service.ado.db.AbstractDbBeanService;
import net.simpleframework.module.news.INewsContextAware;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractNewsService<T extends Serializable> extends AbstractDbBeanService<T>
		implements INewsContextAware {

	protected NewsCategoryService getNewsCategoryService() {
		return (NewsCategoryService) context.getNewsCategoryService();
	}

	protected NewsService getNewsService() {
		return (NewsService) context.getNewsService();
	}

	protected NewsCommentService getCommentService() {
		return (NewsCommentService) context.getCommentService();
	}

	protected NewsAttachmentService getAttachmentService() {
		return (NewsAttachmentService) context.getAttachmentService();
	}
}