package net.simpleframework.module.news;

import net.simpleframework.ctx.service.ado.IADOTreeBeanServiceAware;
import net.simpleframework.module.common.content.ICommentService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface INewsCommentService extends ICommentService<NewsComment>,
		IADOTreeBeanServiceAware<NewsComment> {
}