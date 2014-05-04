package net.simpleframework.module.news;

import java.util.Date;

import net.simpleframework.ado.bean.IDateAwareBean;
import net.simpleframework.ado.bean.IDomainBeanAware;
import net.simpleframework.ado.bean.INameBeanAware;
import net.simpleframework.ado.db.common.EntityInterceptor;
import net.simpleframework.common.ID;
import net.simpleframework.module.common.content.AbstractCategoryBean;
import net.simpleframework.module.common.content.ECategoryMark;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@EntityInterceptor(listenerTypes = { "net.simpleframework.module.log.EntityDeleteLogAdapter" })
public class NewsCategory extends AbstractCategoryBean implements IDomainBeanAware, INameBeanAware,
		IDateAwareBean {
	/* 域，用在在线系统。默认为0 */
	private int domain;

	/* 查看模板 */
	private int viewTemplate;

	/* 名称或编码，唯一 */
	private String name;
	/* 标识 */
	private ECategoryMark mark;

	/* 创建人 */
	private ID userId;
	/* 创建日期 */
	private Date createDate;

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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public ECategoryMark getMark() {
		return mark == null ? ECategoryMark.normal : mark;
	}

	public void setMark(final ECategoryMark mark) {
		this.mark = mark;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	@Override
	public Date getCreateDate() {
		if (createDate == null) {
			createDate = new Date();
		}
		return createDate;
	}

	@Override
	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	private static final long serialVersionUID = -1520445282796635254L;
}
