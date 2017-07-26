package net.simpleframework.module.news.bean;

import java.util.Date;

import net.simpleframework.ado.db.common.EntityInterceptor;
import net.simpleframework.common.ID;
import net.simpleframework.module.common.content.AbstractContentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@EntityInterceptor(listenerTypes = { "net.simpleframework.module.log.EntityUpdateLogAdapter",
		"net.simpleframework.module.log.EntityDeleteLogAdapter" }, columns = { "status", "topic" })
public class News extends AbstractContentBean {

	/* 唯一名称，可为null */
	private String cname;

	/* 域 */
	private String domainId;

	/* 类目id */
	private ID categoryId;

	/* 审核状态 */
	private EAuditStatus astatus;

	/* 主题词 */
	private String keyWords;
	/* 作者 */
	private String author;
	/* 来源 */
	private String source;

	/* 图片标识 */
	private boolean imageMark;
	/* 视频标识 */
	private boolean videoMark;
	/* 视频时长，单位s */
	private int videoTime;

	/* 推荐级别 */
	private int rlevel;

	/* 是否允许评论 */
	private boolean allowComments = true;
	/* 统计信息-评论数。此信息需要和关联表同步 */
	private int comments;
	/* 最后一次更新统计comments的时间 */
	private Date lastCommentDate;

	/* 收费方式 */
	private int fee;
	/* 是否允许建立索引 */
	private boolean indexed = true;

	public String getCname() {
		return cname;
	}

	public void setCname(final String cname) {
		this.cname = cname;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(final String domainId) {
		this.domainId = domainId;
	}

	public ID getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(final ID categoryId) {
		this.categoryId = categoryId;
	}

	public EAuditStatus getAstatus() {
		return astatus == null ? EAuditStatus.none : astatus;
	}

	public void setAstatus(final EAuditStatus astatus) {
		this.astatus = astatus;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(final String keyWords) {
		this.keyWords = keyWords;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public int getRlevel() {
		return rlevel;
	}

	public void setRlevel(final int rlevel) {
		this.rlevel = rlevel;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(final int comments) {
		this.comments = comments;
	}

	public Date getLastCommentDate() {
		return lastCommentDate;
	}

	public void setLastCommentDate(final Date lastCommentDate) {
		this.lastCommentDate = lastCommentDate;
	}

	public boolean isAllowComments() {
		return allowComments;
	}

	public void setAllowComments(final boolean allowComments) {
		this.allowComments = allowComments;
	}

	public boolean isIndexed() {
		return indexed;
	}

	public void setIndexed(final boolean indexed) {
		this.indexed = indexed;
	}

	public boolean isImageMark() {
		return imageMark;
	}

	public void setImageMark(final boolean imageMark) {
		this.imageMark = imageMark;
	}

	public boolean isVideoMark() {
		return videoMark;
	}

	public void setVideoMark(final boolean videoMark) {
		this.videoMark = videoMark;
	}

	public int getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(final int videoTime) {
		this.videoTime = videoTime;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(final int fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return getTopic();
	}

	private static final long serialVersionUID = 580033858128590717L;

	public static int CONTENT_MARK_IMG = 1; // 含有图片 01
	public static int CONTENT_MARK_ATTACH = 2; // 含有附件 10
	public static int CONTENT_MARK_CODE = 4; // 含有代码 100
}
