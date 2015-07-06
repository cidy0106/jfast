package com.xidige.jfast.web;

import java.util.Arrays;
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
	private Map<String,String> params=null;
	/**
	 * 
	 * @param request 原始请求
	 * @param params 附加的参数
	 */
	public DefaultHttpServletRequest(HttpServletRequest request,Map<String, String>params) {
		super(request);
		this.params=params;
	}	
	public DefaultHttpServletRequest(HttpServletRequest request) {
		this(request,null);
	}
	
	private Map<String,Object>paramMap=null;//参数缓存
	@Override
	public Map<String,Object> getParameterMap() {
		if (paramMap!=null) {
			return paramMap;
		}
		paramMap=new HashMap<String, Object>();
		paramMap.putAll(super.getParameterMap());
		for (Iterator<Entry<String, String>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, String> entry = iterator.next();
			Object origParam=paramMap.get(entry.getKey());
			if (origParam==null) {
				paramMap.put(entry.getKey(), entry.getValue());
			}else {
				if (origParam.getClass().isArray()) {
					String oldValues[]=(String[]) origParam;
					String values[]=Arrays.copyOf(oldValues, oldValues.length+1);
					values[oldValues.length]=entry.getValue();
					paramMap.put(entry.getKey(), values);
				}else {
					String []newValues=new String[2];
					newValues[0]=(String) origParam;
					newValues[1]=entry.getValue();
					paramMap.put(entry.getKey(), newValues);
				}
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
		String value=null;
		if (params!=null && params.size()>0) {
			value=params.get(name);
		}
		if (value==null) {
			Map<String, Object> tempParamMap=getParameterMap();
			if (tempParamMap!=null && tempParamMap.size()>0) {
				Object tempValue=tempParamMap.get(name);
				if (tempValue!=null && tempValue.getClass().isArray()) {
					String values[]=(String[])tempValue;
					if (values.length>0) {
						value=values[0];
					}
				}
			}
		}		
		return value;
	}
	@Override
	public String[] getParameterValues(String name) {
		Map<String, Object> tempParamMap=getParameterMap();
		Object tempValue=null;
		String values[]=null;
		if (tempParamMap!=null && (tempValue=tempParamMap.get(name))!=null) {
			if(tempValue.getClass().isArray()){
				values=(String[]) tempValue;
			}else{
				values=new String[]{(String)tempValue};
			}
		}
		return values;
	}
	private Enumeration<String>paramNames=null;
	@Override
	public Enumeration<String> getParameterNames() {
		if (paramNames!=null) {
			return paramNames;
		}
		Map<String, Object> tempParamMap=getParameterMap();
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
