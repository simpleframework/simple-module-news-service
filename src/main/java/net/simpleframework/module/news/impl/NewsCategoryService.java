package net.simpleframework.module.news.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.FilterItems;
import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.common.content.ContentException;
import net.simpleframework.module.news.INewsCategoryService;
import net.simpleframework.module.news.bean.NewsCategory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsCategoryService extends AbstractNewsService<NewsCategory> implements
		INewsCategoryService {

	@Override
	public NewsCategory getBeanByName(final String name) {
		return getBean("name=?", name);
	}

	@Override
	public IDataQuery<NewsCategory> queryChildren(final NewsCategory parent,
			final ColumnData... orderColumns) {
		if (parent == null) {
			final FilterItems items = FilterItems.of();
			items.addIsNull("parentid");
			return queryByParams(items, orderColumns);
		}
		return super.queryChildren(parent, orderColumns);
	}

	@Override
	public void onInit() throws Exception {
		super.onInit();

		addListener(new DbEntityAdapterEx<NewsCategory>() {
			@Override
			public void onBeforeDelete(final IDbEntityManager<NewsCategory> manager,
					final IParamsValue paramsValue) {
				for (final NewsCategory category : coll(manager, paramsValue)) {
					if (_newsService.queryBeans(category, null).getCount() > 0) {
						throw ContentException.of($m("NewsCategoryService.0"));
					}
				}
			}
		});
	}
}
