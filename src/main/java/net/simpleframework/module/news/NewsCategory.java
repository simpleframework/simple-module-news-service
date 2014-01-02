package net.simpleframework.module.news;

import net.simpleframework.ado.bean.IDomainBeanAware;
import net.simpleframework.ado.db.DbEntityTable;
import net.simpleframework.ado.db.common.EntityInterceptor;
import net.simpleframework.module.common.content.AbstractCategoryBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@EntityInterceptor(listenerTypes = { "net.simpleframework.module.log.EntityDeleteLogAdapter" })
public class NewsCategory extends AbstractCategoryBean implements IDomainBeanAware {

	/* 域，用在在线系统。默认为0 */
	private int domain;

	/* 查看模板 */
	private int viewTemplate;

	@Override
	public int getDomain() {
		return domain;
	}

	@Override
	public void setDomain(final int domain) {
		this.domain = domain;
	}

	public int getViewTemplate() {
		return viewTemplate;
	}

	public void setViewTemplate(final int viewTemplate) {
		this.viewTemplate = viewTemplate;
	}

	public static final DbEntityTable TBL = new DbEntityTable(NewsCategory.class, "sf_news_category");

	private static final long serialVersionUID = -1520445282796635254L;
}
