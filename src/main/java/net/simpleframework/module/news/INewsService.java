package net.simpleframework.module.news;

import net.simpleframework.ado.lucene.ILuceneManager;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.common.content.IContentService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface INewsService extends IContentService<News> {

	IDataQuery<News> queryContentBeans(String category);

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
