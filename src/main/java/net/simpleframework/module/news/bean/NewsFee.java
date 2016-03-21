package net.simpleframework.module.news.bean;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.bean.AbstractIdBean;
import net.simpleframework.common.ID;
import net.simpleframework.common.NumberUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsFee extends AbstractIdBean {
	private ID newsId;
	/* 价格 */
	private float price;

	/* 费用状态 */
	private EFeeStatus status;
	/* 描述 */
	private String description;

	public ID getNewsId() {
		return newsId;
	}

	public void setNewsId(final ID newsId) {
		this.newsId = newsId;
	}

	public float getPrice() {
		return NumberUtils.toFloat(price, 2);
	}

	public void setPrice(final float price) {
		// 不允许为负
		this.price = Math.max(price, 0f);
	}

	public EFeeStatus getStatus() {
		return status == null ? EFeeStatus.running : status;
	}

	public void setStatus(final EFeeStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public static enum EFeeStatus {
		running {
			@Override
			public String toString() {
				return $m("EFeeStatus.running");
			}
		},
		abort {
			@Override
			public String toString() {
				return $m("EFeeStatus.abort");
			}
		}
	}

	private static final long serialVersionUID = 6535341618367647105L;
}
