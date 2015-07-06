package com.xidige.jfast.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于资源路径处理的工具类
 * @author kime
 *
 */
public class UriUtil {
	/**
	 * 获取请求的uri部分,不包含web目录和?后的
	 * @param request
	 * @return
	 */
	public static String getRequestUri(HttpServletRequest request){
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (contextPath != null && contextPath.length() > 0) {
		    uri = uri.substring(contextPath.length());
		}
		return uri;
	}
}
