package com.xidige.jfast.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xidige.jfast.core.ActionInvocation;
import com.xidige.jfast.core.Config;
import com.xidige.jfast.core.ConfigFactory;
import com.xidige.jfast.core.StaticConstants;
import com.xidige.jfast.core.UriInfo;
/**
 * 加载同步场景的应用 * 
 * nnd千万不要问我什么是同步场景，就当是我自己的专有名词即可，或者去看看servlet3异步吧：）
 * 
 * 需要加载第一个配置类，继承自configloader接口
 * 
 * @author kime
 *
 */
public class DefaultFilter implements Filter,StaticConstants {
	private Config config=null;
	private Dispatcher dispatcher=null;
	public void init(FilterConfig filterConfig) throws ServletException {
		config=new Config();
		dispatcher=new Dispatcher(config);
		String initFactoryStr=filterConfig.getInitParameter(INITPARAMS.JF_CONFIG_FACTORY);
		//i don't want "null" here~~
		if (initFactoryStr==null || "".equals(initFactoryStr)) {
			throw new ServletException("jf.param.init.configfactory Cannot be EMPTY");
		}
		try {
			Class<?>loaderClass= Class.forName(initFactoryStr);
			if (ConfigFactory.class.isAssignableFrom(loaderClass)) {
				ConfigFactory configFactory=(ConfigFactory) loaderClass.newInstance();
				dispatcher.setUp(configFactory.getConfigLoaders(filterConfig));
			}			
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		} catch (InstantiationException e) {
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			throw new ServletException(e);
		} 
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse resp=(HttpServletResponse) response;
		dispatcher.initCharset(req, resp);
		UriInfo uriInfo= dispatcher.parseUri(req);
		if (uriInfo!=null) {
			ActionInvocation actionInvocation= dispatcher.findAction(uriInfo);
			if (actionInvocation!=null) {
				HttpServletRequest newReq=new DefaultHttpServletRequest(req,uriInfo.getParameters(),config.getHttpSessionCreator());
				dispatcher.initActionContext(newReq, resp);
				dispatcher.startAction(actionInvocation);
				dispatcher.cleanRequest();
				return;
			}
		}
		dispatcher.cleanRequest();
		chain.doFilter(request, response);
	}

	public void destroy() {
	}

}
