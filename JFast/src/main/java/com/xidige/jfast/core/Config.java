package com.xidige.jfast.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 配置
 * @author kime
 *
 */
public class Config {
	private List<Interceptor> interceptors=new ArrayList<Interceptor>();//拦截器
	private List<UriParser>uriParsers=new ArrayList<UriParser>();//uri解析器
	private List<ActionLookup>actionLookups=new ArrayList<ActionLookup>();//action搜索器
	private Map<String, Object>attrs=new HashMap<String, Object>();
	private Config readonlyObject=null;
	
	
	/**
	 * 获取一个只读属性的配置
	 * @param cachefirst
	 * @return
	 */
	public Config readonlyConfig(boolean cachefirst){
		if (cachefirst &&readonlyObject!=null) {
			return readonlyObject;
		}
		readonlyObject=new Config();
		readonlyObject.attrs=Collections.unmodifiableMap(attrs);
		return readonlyObject;
	}
	public void addInterceptor(Interceptor interceptor){
		interceptors.add(interceptor);
	}
	public void addUriParser(UriParser uriParser){
		uriParsers.add(uriParser);
	}
	public void addActionLookup(ActionLookup actionLookup){
		actionLookups.add(actionLookup);
	}
	
	
	
	
	public List<Interceptor> getInterceptors() {
		return interceptors;
	}
	public List<UriParser> getUriParsers() {
		return uriParsers;
	}
	public List<ActionLookup> getActionLookups() {
		return actionLookups;
	}
	public void setAttr(String key,Object value){
		attrs.put(key, value);
	}
	public Object getAttr(String key){
		return attrs.get(key);
	}
	
}
