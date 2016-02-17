package net.simpleframework.module.news;

import java.util.Date;

import net.simpleframework.ado.bean.AbstractDateAwareBean;
import net.simpleframework.common.ID;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsRecommend extends AbstractDateAwareBean {
	/* 主题id */
	private ID newsId;
	/* 推荐状态 */
	private ERecommendStatus status;

	/* 推荐级别 */
	private int rlevel;

	/* 开始时间 */
	private Date dstartDate;
	/* 结束时间 */
	private Date dendDate;

	public ID getNewsId() {
		return newsId;
	}

	public void setNewsId(final ID newsId) {
		this.newsId = newsId;
	}

	public ERecommendStatus getStatus() {
		return status;
	}

	public void setStatus(final ERecommendStatus status) {
		this.status = status;
	}

	public int getRlevel() {
		return rlevel;
	}

	public void setRlevel(final int rlevel) {
		this.rlevel = rlevel;
	}

	public Date getDstartDate() {
		return dstartDate;
	}

	public void setDstartDate(final Date dstartDate) {
		this.dstartDate = dstartDate;
	}

	public Date getDendDate() {
		return dendDate;
	}

	public void setDendDate(final Date dendDate) {
		this.dendDate = dendDate;
	}

	public static enum ERecommendStatus {
		ready,

		running,

		complete,

		abort
	}

	private static final long serialVersionUID = 6285609326593701390L;
}
