package net.simpleframework.module.news;

import static net.simpleframework.common.I18n.$m;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public enum ENewsAttach {
	content {
		@Override
		public String toString() {
			return $m("ENewsAttach.content");
		}
	},
	icon {
		@Override
		public String toString() {
			return $m("ENewsAttach.icon");
		}
	}
}
