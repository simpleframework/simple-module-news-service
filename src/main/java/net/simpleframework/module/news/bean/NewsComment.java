package net.simpleframework.module.news.bean;

import net.simpleframework.ado.bean.ITreeBeanAware;
import net.simpleframework.ado.db.common.EntityInterceptor;
import net.simpleframework.common.ID;
import net.simpleframework.module.common.content.AbstractComment;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@EntityInterceptor(listenerTypes = { "net.simpleframework.module.log.EntityDeleteLogAdapter" })
public class NewsComment extends AbstractComment implements ITreeBeanAware {
	/* 父id */
	private ID parentId;

	@Override
	public ID getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(final ID parentId) {
		this.parentId = parentId;
	}

	private static final long serialVersionUID = 5679649774061437068L;
}
