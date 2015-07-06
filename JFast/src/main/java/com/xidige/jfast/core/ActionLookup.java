package com.xidige.jfast.core;
/**
 * action搜索器
 * @author kime
 *
 */
public interface ActionLookup {
	public ActionInvocation lookup(UriInfo uriInfo);
}