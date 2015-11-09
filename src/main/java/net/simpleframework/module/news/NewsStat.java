package net.simpleframework.module.news;

import net.simpleframework.ado.bean.AbstractIdBean;
import net.simpleframework.common.ID;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsStat extends AbstractIdBean {
	/* 类目id */
	private ID categoryId;
	/* 域id */
	private ID domainId;

	/* 总数 */
	private int nums;

	/* 发布的数量 */
	private int nums_publish;
	/* 删除的数量 */
	private int nums_delete;

	public ID getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(final ID categoryId) {
		this.categoryId = categoryId;
	}

	public ID getDomainId() {
		return domainId;
	}

	public void setDomainId(final ID domainId) {
		this.domainId = domainId;
	}

	public int getNums() {
		return nums;
	}

	public void setNums(final int nums) {
		this.nums = nums;
	}

	public int getNums_publish() {
		return nums_publish;
	}

	public void setNums_publish(final int nums_publish) {
		this.nums_publish = nums_publish;
	}

	public int getNums_delete() {
		return nums_delete;
	}

	public void setNums_delete(final int nums_delete) {
		this.nums_delete = nums_delete;
	}

	private static final long serialVersionUID = 247098680385743430L;
}
