package net.simpleframework.module.news.impl;

import java.util.List;

import net.simpleframework.common.ID;
import net.simpleframework.common.coll.ArrayUtils;
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
		final StringBuilder sql = new StringBuilder("categoryid=?");
		final List<Object> params = ArrayUtils.toParams(categoryId);
		if (domainId == null) {
			sql.append(" and domainid is null");
		} else {
			sql.append(" and domainid=?");
			params.add(categoryId);
		}
		NewsStat stat = getBean(sql, params.toArray());
		if (stat == null) {
			stat = createBean();
			stat.setCategoryId(categoryId);
			stat.setDomainId(domainId);
			insert(stat);
		}
		return stat;
	}

	void setNewsStat(final NewsStat stat) {
	}
}
