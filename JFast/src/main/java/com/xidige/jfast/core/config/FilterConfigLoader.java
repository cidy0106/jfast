package com.xidige.jfast.core.config;

import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import com.xidige.jfast.core.Config;
import com.xidige.jfast.core.ConfigLoader;
import com.xidige.jfast.core.StaticConstants;
import com.xidige.jfast.core.exception.ConfigException;
/**
 * filter中的配置也读取到config中
 * @author kime
 *
 */
public class FilterConfigLoader implements ConfigLoader,StaticConstants {
	private FilterConfig filterConfig;
	public FilterConfigLoader(FilterConfig filterConfig) {
		this.filterConfig=filterConfig;
	}
	public void load(Config jfConfig) throws ConfigException {
		Enumeration ps=filterConfig.getInitParameterNames();
		while (ps.hasMoreElements()) {
			String name = (String) ps.nextElement();
			jfConfig.setAttr(name, filterConfig.getInitParameter(name));
		}
		
		ServletContext temp=filterConfig.getServletContext();
		jfConfig.setAttr(JF_SERVLET_CONTEXT, temp);
		jfConfig.setAttr(JF_CONTEXT_PATH, temp.getContextPath());
		jfConfig.setAttr(JF_ROOL_REAL_PATH, temp.getRealPath("/"));
	}
	

}
