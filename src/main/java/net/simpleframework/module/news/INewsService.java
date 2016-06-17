package net.simpleframework.module.news;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.FilterItems;
import net.simpleframework.ado.lucene.ILuceneManager;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.ID;
import net.simpleframework.common.TimePeriod;
import net.simpleframework.module.common.content.AbstractCategoryBean;
import net.simpleframework.module.common.content.AbstractContentBean.EContentStatus;
import net.simpleframework.module.common.content.IContentService;
import net.simpleframework.module.news.bean.News;
import net.simpleframework.module.news.bean.NewsCategory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface INewsService extends IContentService<News> {

	IDataQuery<News> queryBeans(AbstractCategoryBean category, ID domainId, EContentStatus status,
			TimePeriod timePeriod, FilterItems filterItems, ColumnData... orderColumns);

	IDataQuery<News> queryContentBeans(String category);

	/**
	 * 查询用户创建的
	 * 
	 * @param userId
	 * @param category
	 * @param status
	 * @return
	 */
	IDataQuery<News> queryContentBeans(ID userId, NewsCategory category, EContentStatus status);

	/**
	 * 查找图片新闻
	 * 
	 * @param category
	 * @return
	 */
	IDataQuery<News> queryImageNews(NewsCategory category);

	/**
	 * 查找视频新闻
	 * 
	 * @param category
	 * @return
	 */
	IDataQuery<News> queryVideoNews(NewsCategory category);

	IDataQuery<News> queryRecommendBeans(NewsCategory category, TimePeriod timePeriod);

	/**
	 * 根据名称获取新闻对象
	 * 
	 * @param name
	 * @return
	 */
	News getBeanByName(String name);

	/**
	 * 获取全文检索服务
	 * 
	 * @return
	 */
	ILuceneManager getLuceneService();
}
