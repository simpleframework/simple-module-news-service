package net.simpleframework.module.news;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ctx.IModuleContext;
import net.simpleframework.module.news.impl.NewsContext;
import net.simpleframework.organization.OrganizationRef;
import net.simpleframework.organization.RolenameConst;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class NewsOrganizationRef extends OrganizationRef {

	@Override
	public void onInit(final IModuleContext context) throws Exception {
		super.onInit(context);

		NewsContext.ROLE_NEWS_MANAGER = RolenameConst.toUniqueRolename(
				RolenameConst.ROLECHART_SYSTEM, "news_manager");
		createRole_SystemChart(NewsContext.ROLE_NEWS_MANAGER, $m("NewsOrganizationRef.0"));
	}
}
