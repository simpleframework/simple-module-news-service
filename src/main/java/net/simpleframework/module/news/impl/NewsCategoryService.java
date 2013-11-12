package net.simpleframework.module.news.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ctx.ModuleException;
import net.simpleframework.module.news.INewsCategoryService;
import net.simpleframework.module.news.NewsCategory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsCategoryService extends AbstractNewsService<NewsCategory> implements
		INewsCategoryService {

	@Override
	public NewsCategory getBeanByName(final String name) {
		return getBean("name=?", name);
	}

	@Override
	public void onInit() throws Exception {
		addListener(new DbEntityAdapterEx() {
			@Override
			public void onBeforeDelete(final IDbEntityManager<?> service,
					final IParamsValue paramsValue) {
				for (final NewsCategory category : coll(paramsValue)) {
					if (getNewsService().query(category, null).getCount() > 0) {
						throw ModuleException.of($m("NewsCategoryService.0"));
					}
				}
			}

			@Override
			public void onBeforeUpdate(final IDbEntityManager<?> manager, final String[] columns,
					final Object[] beans) {
				super.onBeforeUpdate(manager, columns, beans);
				for (final Object o : beans) {
					assertParentId((NewsCategory) o);
				}
			}
		});
	}
}
