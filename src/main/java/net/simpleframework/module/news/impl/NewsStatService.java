package net.simpleframework.module.news.impl;

import java.util.List;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.BeanUtils.PropertyWrapper;
import net.simpleframework.common.Convert;
import net.simpleframework.common.ID;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.module.common.content.EContentStatus;
import net.simpleframework.module.news.INewsStatService;
import net.simpleframework.module.news.bean.News;
import net.simpleframework.module.news.bean.NewsStat;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsStatService extends AbstractNewsService<NewsStat> implements INewsStatService {

	@Override
	public int getAllNums(final ID categoryId, final String prop) {
		return sum(prop, "categoryid=?", categoryId).intValue();
	}

	@Override
	public int getAllNums_delete(final ID domainId) {
		if (domainId == null) {
			return sum("nums_delete").intValue();
		} else {
			return sum("nums_delete", "domainid=? or domainid is null", domainId).intValue();
		}
	}

	@Override
	public NewsStat getNewsStat(final ID categoryId, final ID domainId) {
		final StringBuilder sql = new StringBuilder("categoryid=?");
		final List<Object> params = ArrayUtils.toParams(categoryId);
		if (domainId != null) {
			sql.append(" and domainid=?");
			params.add(domainId);
		} else {
			sql.append(" and domainid is null");
		}
		NewsStat stat = getBean(sql, params.toArray());
		if (stat == null) {
			stat = createBean();
			stat.setCategoryId(categoryId);
			stat.setDomainId(domainId);
			setNewsStat(stat);
			insert(stat);
		}
		return stat;
	}

	void reset(final NewsStat stat) {
		for (final PropertyWrapper p : BeanUtils.getProperties(NewsStat.class).values()) {
			if ("int".equals(p.type.getName())) {
				BeanUtils.setProperty(stat, p.name, 0);
			}
		}
	}

	void setNewsStat(final NewsStat stat) {
		final List<Object> params = ArrayUtils.toParams(stat.getCategoryId());
		final StringBuilder sql = new StringBuilder("select status, count(*) as c from ").append(
				getTablename(News.class)).append(" n where n.categoryid=?");
		final ID domainId = stat.getDomainId();
		if (domainId != null) {
			sql.append(" and n.domainid=?");
			params.add(domainId);
		} else {
			sql.append(" and n.domainid is null");
		}
		sql.append(" group by n.status");
		final IDataQuery<Map<String, Object>> dq = getQueryManager().query(sql, params.toArray());
		int nums = 0;
		Map<String, Object> data;
		while ((data = dq.next()) != null) {
			final int c = Convert.toInt(data.get("c"));
			final EContentStatus status = Convert.toEnum(EContentStatus.class, data.get("status"));
			if (status != null) {
				final String prop = "nums_" + status.name();
				if (BeanUtils.hasProperty(stat, prop)) {
					BeanUtils.setProperty(stat, prop, c);
				}
			}
			nums += c;
		}
		stat.setNums(nums);
	}
}
