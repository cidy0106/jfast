package com.xidige.jfast.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.xidige.jfast.web.session.HttpSessionCreator;
/**
 * 改写原有的request实现
 * 
 * 重新解析出uri中的参数（比如：/action/method/arg/val1/arg/val2/×/×）
 * @author kime
 *
 */
public class DefaultHttpServletRequest extends HttpServletRequestWrapper{
	/**
	 * 外部传进来的,可能是自定义解析出来的参数，会合并到原来web容器解析出的参数上去，这里的参数优先比容器的高
	 */
	private Map<String,List<String>> params=null;
	private HttpSessionCreator httpSessionCreator;
	
	private final static HttpSessionCreator defaultHttpSessionCreator=new HttpSessionCreator() {
		@Override
		public HttpSession doCreateSession(RequestContext requestContext, boolean created) {
			return requestContext.getRequest().getSession(created);
		}
	};
	/**
	 * 
	 * @param request 原始请求
	 * @param params 附加的参数
	 */
	public DefaultHttpServletRequest(HttpServletRequest request,Map<String, List<String>>params,HttpSessionCreator httpSessionCreator) {
		super(request);
		this.params=params;
		if (httpSessionCreator==null) {
			this.httpSessionCreator=defaultHttpSessionCreator;
		}else{
			this.httpSessionCreator=httpSessionCreator;
		}
	}	
	public DefaultHttpServletRequest(HttpServletRequest request) {
		this(request,null,null);
	}
	
	private Map<String,String[]>paramMap=null;//参数缓存
	@Override
	public Map<String,String[]> getParameterMap() {
		if (paramMap!=null && !paramMap.isEmpty()) {
			return paramMap;
		}
		
		//把容器的参数附加到自定义参数后面
		Map<String, String[]> containerMap= super.getParameterMap();
		if(containerMap!=null){
			if(params==null){
				params=new HashMap<String, List<String>>();
			}
			
			for (Iterator<Entry<String, String[]>> iterator = containerMap.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, String[]> entry = iterator.next();
				String[]tmpVals=entry.getValue();
				if(tmpVals==null || tmpVals.length==0){
					continue;
				}
				
				List<String>vals= params.get(entry.getKey());
				if(vals==null){
					vals=new ArrayList<String>();
					params.put(entry.getKey(), vals);
				}
				for (int i = 0; i < tmpVals.length; i++) {
					vals.add(tmpVals[i]);
				}
			}
		}
		
		//list转到数组去
		if(params!=null && params.size()>0){
			paramMap=new HashMap<String, String[]>();
			for (Iterator<Entry<String, List<String>>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, List<String>> entry = iterator.next();
				List<String>tmpVals=entry.getValue();
				if(tmpVals!=null && !tmpVals.isEmpty()){
					String[]cVals=new String[tmpVals.size()];
					tmpVals.toArray(cVals);
					paramMap.put(entry.getKey(), cVals);
				}else{
					paramMap.put(entry.getKey(), null);
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
		Map<String, String[]> tempParamMap=this.getParameterMap();
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
		Map<String, String[]> tempParamMap=this.getParameterMap();
		if(tempParamMap!=null){
			return tempParamMap.get(name);
		}
		return null;
	}
	private Enumeration<String>paramNames=null;
	@Override
	public Enumeration<String> getParameterNames() {
		if (paramNames!=null) {
			return paramNames;
		}
		Map<String, String[]> tempParamMap=this.getParameterMap();
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
		return httpSessionCreator.doCreateSession(RequestContext.getContext(),create);
	}
}
