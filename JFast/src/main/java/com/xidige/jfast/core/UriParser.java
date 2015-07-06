package com.xidige.jfast.core;

import javax.servlet.http.HttpServletRequest;


/**
 * url映射与解析接口
 * 
 * @author kime
 *
 */
public interface UriParser {
	/**
	 * 
	 * @param request 当前请求
	 * @param result 解析结果存放到这里
	 * @return
	 */
	public UriInfo parseUri(HttpServletRequest request);
}
