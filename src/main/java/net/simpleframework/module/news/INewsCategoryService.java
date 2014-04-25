package net.simpleframework.module.news;

import net.simpleframework.ctx.service.ado.ITreeBeanServiceAware;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface INewsCategoryService extends IDbBeanService<NewsCategory>,
		ITreeBeanServiceAware<NewsCategory> {

	/**
	 * 根据名字获取类目对象
	 * 
	 * @param name
	 * @return
	 */
	NewsCategory getBeanByName(String name);
}
