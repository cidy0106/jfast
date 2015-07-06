package com.xidige.jfast.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xidige.jfast.core.ActionInvocation;
import com.xidige.jfast.core.ActionLookup;
import com.xidige.jfast.core.ActionProxy;
import com.xidige.jfast.core.Config;
import com.xidige.jfast.core.ConfigLoader;
import com.xidige.jfast.core.StaticConfigs;
import com.xidige.jfast.core.StaticConstants;
import com.xidige.jfast.core.UriInfo;
import com.xidige.jfast.core.UriParser;

/**
 * 调度管理器
 * 负责整体调度功能
 * 
 * 对于大部分旧的单线程,可以直接绑定到请求线程中,这不会发生什么问题;
 * 但是,对于新的实现,基本都是异步的,所以不能再直接绑定到请求线程,而需要另起处理线程,等有结果后再反馈给服务器,这里需要注意的是 线程绑定,请求上下文的传递;
 * @author kime
 *
 */
public class Dispatcher implements StaticConstants,StaticConfigs{	
	private Config config;//全局配置	
	private boolean forceEncoding=false;
	private String encoding="UTF-8";
	
	public Dispatcher(Config config){
		this.config=config;
		init();
	}
	private void init(){
		Object temp=null;
		if ((temp=config.getAttr(JF_REQUEST_CHARACTER_ENCODING_FORCE))!=null) {
			forceEncoding=Boolean.valueOf(String.valueOf(temp));
		}
		if ((temp=config.getAttr(JF_REQUEST_CHARACTER_ENCODING))!=null) {
			encoding=String.valueOf(temp);
		}		
	}
	/**
	 * 编码设置
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	public void initCharset(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		if (this.encoding != null
				&& (this.forceEncoding || request.getCharacterEncoding() == null)) {
			request.setCharacterEncoding(encoding);
			if (this.forceEncoding) {
				response.setCharacterEncoding(encoding);
			}
		}
	}
	/**
	 * 解析uri
	 * @param request
	 * @return
	 */
	public UriInfo parseUri(HttpServletRequest request){
		UriInfo result=null;
		List<UriParser>uriParsers=config.getUriParsers();
		for (int i = uriParsers.size()-1; i>=0; i--) {
			result=uriParsers.get(i).parseUri(request);
			if (result!=null) {
				return result;
			}
		}
		return null;
	}
	/**
	 * 搜索action
	 * @param uriInfo
	 */
	public ActionInvocation findAction(UriInfo uriInfo){
		ActionInvocation actionProxy=null;
		List<ActionLookup>lookups=config.getActionLookups();
		for (int i = lookups.size()-1; i >=0 ; i--) {
			actionProxy=lookups.get(i).lookup(uriInfo);
			if (actionProxy!=null) {
				return actionProxy;
			}
		}
		return null;
	}
	/**
	 * 包装一下action
	 * @param actionProxy
	 */
	public void wrapAction(ActionInvocation actionInvocation){
		actionInvocation.setInterceptors(config.getInterceptors());
	}
	public void initActionContext(HttpServletRequest request, HttpServletResponse response){
		RequestContext actionContext=new RequestContext(request, response);
		actionContext.setConfig(config.readonlyConfig(true));
		RequestContext.bindToCurrentThread(actionContext);
	}
	/**
	 * 启动action调用链
	 * @param actionProxy
	 */
	public void startAction(ActionProxy actionProxy){
		actionProxy.invoke();
	}
	public void cleanRequest(){
		RequestContext actionContext= RequestContext.getContext();
		if (actionContext!=null) {
			actionContext.destroy();
		}
	}
	/**
	 * 用给定的配置加载器  加载框架环境
	 * @param configLoaders
	 */
	public void setUp(List<ConfigLoader> configLoaders) {
		for (ConfigLoader configLoader : configLoaders) {
			configLoader.load(config);
		} 
	}	
}
