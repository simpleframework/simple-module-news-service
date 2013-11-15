package net.simpleframework.module.news;

import net.simpleframework.ado.db.DbEntityTable;
import net.simpleframework.ado.db.common.EntityInterceptor;
import net.simpleframework.module.common.content.AbstractCategoryBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@EntityInterceptor(listenerTypes = { "net.simpleframework.module.log.EntityDeleteLogAdapter" })
public class NewsCategory extends AbstractCategoryBean {

	/* 查看模板 */
	private int viewTemplate;

	public int getViewTemplate() {
		return viewTemplate;
	}

	public void setViewTemplate(final int viewTemplate) {
		this.viewTemplate = viewTemplate;
	}

	public static final DbEntityTable TBL = new DbEntityTable(NewsCategory.class, "sf_news_category");

	private static final long serialVersionUID = -1520445282796635254L;
}
