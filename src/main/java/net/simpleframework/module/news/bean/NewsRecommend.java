package net.simpleframework.module.news.bean;

import static net.simpleframework.common.I18n.$m;

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

	private ID newsId;
	/* 推荐状态 */
	private ERecommendStatus status;

	/* 推荐级别 */
	private int rlevel;

	/* 设计开始时间和设计结束时间 */
	private Date dstartDate, dendDate;

	/* 描述 */
	private String description;

	public ID getNewsId() {
		return newsId;
	}

	public void setNewsId(final ID newsId) {
		this.newsId = newsId;
	}

	public ERecommendStatus getStatus() {
		return status == null ? ERecommendStatus.ready : status;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public static enum ERecommendStatus {
		ready {
			@Override
			public String toString() {
				return $m("ERecommendStatus.ready");
			}
		},
		running {
			@Override
			public String toString() {
				return $m("ERecommendStatus.running");
			}
		},
		complete {
			@Override
			public String toString() {
				return $m("ERecommendStatus.complete");
			}
		},
		abort {
			@Override
			public String toString() {
				return $m("ERecommendStatus.abort");
			}
		}
	}

	private static final long serialVersionUID = 6285609326593701390L;
}
