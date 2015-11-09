package net.simpleframework.module.news.impl;

import net.simpleframework.common.ID;
import net.simpleframework.module.news.INewsStatService;
import net.simpleframework.module.news.NewsStat;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsStatService extends AbstractNewsService<NewsStat> implements INewsStatService {

	@Override
	public NewsStat getNewsStat(final ID categoryId, final ID domainId) {
		final StringBuilder sql = new StringBuilder();
		return getBean(sql);
	}
}
