package net.simpleframework.module.news.bean;

import net.simpleframework.ado.db.common.EntityInterceptor;
import net.simpleframework.module.common.content.Attachment;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@EntityInterceptor(listenerTypes = { "net.simpleframework.module.log.EntityDeleteLogAdapter" })
public class NewsAttachment extends Attachment {

	@Override
	public int getImodule() {
		return 11;
	}

	private static final long serialVersionUID = 5420987166480558990L;
}
