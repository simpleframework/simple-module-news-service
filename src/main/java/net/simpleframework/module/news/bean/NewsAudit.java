package net.simpleframework.module.news.bean;

import net.simpleframework.ado.bean.AbstractUserAwareBean;
import net.simpleframework.ado.db.common.EntityInterceptor;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@EntityInterceptor(listenerTypes = { "net.simpleframework.module.log.EntityDeleteLogAdapter" })
public class NewsAudit extends AbstractUserAwareBean {

	private static final long serialVersionUID = 1390020297704889436L;

}
