package com.xidige.jfast.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xidige.jfast.core.Config;

/**
 * 单次请求上下文 绑定到当前线程中,可以直接用get方法取到与当前 线程对象绑定的实例
 * 
 * @author kime
 *
 */
public class RequestContext {
	private static ThreadLocal<RequestContext>threadLocal=new ThreadLocal<RequestContext>();

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private ServletContext servletContext = null;
	private Config config;

	public RequestContext(HttpServletRequest request,
			HttpServletResponse response) {
		this.request=request;
		this.response=response;
	}

	public void destroy() {
		this.request = null;
		this.response = null;
		this.servletContext = null;
		this.config=null;
		threadLocal.set(null);
	}
	public void redirect(String view) throws ServletException, IOException{
		request.getRequestDispatcher(view).forward(request, response);
	}

	/**
	 * 先用bindToCurrentThread绑定到线程
	 * @return
	 */
	public static RequestContext getContext(){
		return threadLocal.get();
	}
	/**
	 * 绑定到当前线程
	 * @param actionContext
	 */
	public static void bindToCurrentThread(RequestContext actionContext){
		threadLocal.set(actionContext);
	}
	
	// //////////////////////getter or setter //////////////////////

	public Object getAttr(String key) {
		return request.getAttribute(key);
	}

	public void setAttr(String key, Object value) {
		request.setAttribute(key, value);
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void setConfig(Config readonlyConfig) {
		this.config=readonlyConfig;
	}
	public Config getConfig() {
		return config;
	}
}
