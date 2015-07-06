package com.xidige.jfast.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xidige.jfast.core.exception.RenderException;

public abstract class AbstractRender implements Render {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected String viewPath;
	protected String templateDir;
	/**
	 * 
	 * @param request
	 * @param response
	 * @param templateDir 模板绝对路径，可以为null
	 * @return
	 */
	public AbstractRender setUp(HttpServletRequest request,HttpServletResponse response,String templateDir){
		this.request=request;
		this.response=response;
		this.templateDir=templateDir;
		return this;
	}
	public AbstractRender setContentType(String contentType){
		response.setContentType(contentType);
		return this;
	}
	public AbstractRender setStatusCode(int statusCode){
		response.setStatus(statusCode);
		return this;
	}
	public AbstractRender setView(String view){
		this.viewPath=view;
		return this;
	}
	
	public abstract void render() throws RenderException ;
	
}
