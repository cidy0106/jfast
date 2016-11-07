package com.xidige.jfast.web;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
/**
 * 改写原有的request实现
 * 
 * 1,引入自己的session，使用第三方缓存组件重新实现session的存取
 * 2,引入自己的attribute，使用第三方缓存组件重新实现attribute的存取
 * 3,重新解析出uri中的参数（比如：/action/method/arg/val1/arg/val2/×/×）
 * @author kime
 *
 */
public class DefaultHttpServletRequest extends HttpServletRequestWrapper implements HttpServletRequest {
	private Map<String,String[]> params=null;
	/**
	 * 
	 * @param request 原始请求
	 * @param params 附加的参数
	 */
	public DefaultHttpServletRequest(HttpServletRequest request,Map<String, String[]>params) {
		super(request);
		this.params=params;
	}	
	public DefaultHttpServletRequest(HttpServletRequest request) {
		this(request,null);
	}
	
	private Map<String,String[]>paramMap=null;//参数缓存
	@Override
	public Map<String,String[]> getParameterMap() {
		if (paramMap!=null) {
			return paramMap;
		}
		paramMap=new HashMap<String, String[]>();
		paramMap.putAll(super.getParameterMap());
		for (Iterator<Entry<String, String[]>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, String[]> entry = iterator.next();
			String[] origParam=paramMap.get(entry.getKey());
			if (origParam==null ||origParam.length==0) {
				paramMap.put(entry.getKey(), entry.getValue());
			}else {
				String entryValues[]=entry.getValue();
				if(entryValues==null || entryValues.length==0){
					continue;
				}
				String newValues[]=new String[origParam.length+entryValues.length];
				int index=0;
				for (; index < entryValues.length; index++) {
					newValues[index]=entryValues[index];
				}
				for (int i = 0; i < origParam.length; i++) {
					newValues[index++]=origParam[i];
				}
				
				paramMap.put(entry.getKey(), newValues);					
			}
		}
		return paramMap; 
	}
	/**
	 * 优先返回uri解析的值;
	 * 如果原来是数组的,则返回第一个值
	 */
	@Override
	public String getParameter(String name) {
		if (name==null) {
			return name;
		}
		Map<String, String[]> tempParamMap=getParameterMap();
		if (tempParamMap!=null && tempParamMap.size()>0) {
			String[] tempValue=tempParamMap.get(name);
			if (tempValue!=null && tempValue.length>0) {
				return tempValue[0];
			}
		}
		return null;
	}
	@Override
	public String[] getParameterValues(String name) {
		Map<String, String[]> tempParamMap=getParameterMap();
		return tempParamMap.get(name);
	}
	private Enumeration<String>paramNames=null;
	@Override
	public Enumeration<String> getParameterNames() {
		if (paramNames!=null) {
			return paramNames;
		}
		Map<String, String[]> tempParamMap=getParameterMap();
		if (tempParamMap!=null) {
			paramNames=Collections.enumeration(tempParamMap.keySet());
		}
		return paramNames;
	}
	
	
	@Override
	public HttpSession getSession() {		
		return this.getSession(true);
	}
	@Override
	public HttpSession getSession(boolean create) {
		HttpSession session=super.getSession(create);
		if(session!=null){
			return DefaultHttpSessionHandler.wrapSession(session);
		}
		return null;
	}
}
