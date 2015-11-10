package net.simpleframework.module.news;

import net.simpleframework.common.ID;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface INewsStatService extends IDbBeanService<NewsStat> {

	/**
	 * 获取统计对象
	 * 
	 * @param categoryId
	 * @param domainId
	 * @return
	 */
	NewsStat getNewsStat(ID categoryId, ID domainId);

	int getNewsStat_delete(ID domainId);
}