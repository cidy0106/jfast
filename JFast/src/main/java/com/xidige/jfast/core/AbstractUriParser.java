package com.xidige.jfast.core;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractUriParser implements UriParser {
	/**
	 * 获取请求的uri部分,不包含web目录和?后的
	 * @param request
	 * @return
	 */
	public String getRequestUri(HttpServletRequest request){
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (contextPath != null && contextPath.length() > 0) {
		    uri = uri.substring(contextPath.length());
		}
		return uri;
	}
	public abstract UriInfo parseUri(HttpServletRequest request);

}
