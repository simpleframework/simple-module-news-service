package net.simpleframework.module.news;

import static net.simpleframework.common.I18n.$m;
import static net.simpleframework.module.news.impl.NewsContext.ROLE_NEWS_MANAGER;
import net.simpleframework.ctx.IModuleContext;
import net.simpleframework.organization.ERoleType;
import net.simpleframework.organization.OrganizationRef;
import net.simpleframework.organization.RolenameW;

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

		ROLE_NEWS_MANAGER = RolenameW.toUniqueRolename(RolenameW.ROLECHART_ORG_DEFAULT, "newsmgr");
		RolenameW.registRole("newsmgr", $m("NewsOrganizationRef.0"), null, ERoleType.normal);
	}
}
