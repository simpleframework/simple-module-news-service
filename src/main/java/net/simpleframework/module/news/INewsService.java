package net.simpleframework.module.news;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.FilterItems;
import net.simpleframework.ado.lucene.ILuceneManager;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.common.bean.TimePeriod;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;
import net.simpleframework.module.common.content.EContentStatus;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface INewsService extends IDbBeanService<News> {

	/**
	 * 按指定条件查找
	 * 
	 * @param category
	 *           类目
	 * @param status
	 *           状态
	 * @param timePeriod
	 *           时间范围
	 * @param filterItems
	 *           其它条件
	 * @param orderColumns
	 *           排序
	 * @return
	 */
	IDataQuery<News> query(NewsCategory category, EContentStatus status, TimePeriod timePeriod,
			FilterItems filterItems, ColumnData... orderColumns);

	IDataQuery<News> query(NewsCategory category, EContentStatus status, TimePeriod timePeriod,
			FilterItems filterItems);

	IDataQuery<News> query(NewsCategory category, EContentStatus status);

	IDataQuery<News> queryNews(NewsCategory category, TimePeriod timePeriod,
			ColumnData... orderColumns);

	IDataQuery<News> queryNews(String category);

	/**
	 * 查找图片新闻
	 * 
	 * @param category
	 * @return
	 */
	IDataQuery<News> queryImageNews(NewsCategory category);

	/**
	 * 获取指定类目新闻的数量
	 * 
	 * @param categoryId
	 * @return
	 */
	int count(NewsCategory category);

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
