package com.xidige.jfast.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.xidige.jfast.core.exception.ActionInvocationException;

/**
 * action的调用代理
 * 每一次action都有其自己的代理实例
 * action方法必须是无参数方法
 * 
 * @author kime
 *
 */
public class ActionInvocation implements ActionProxy{
	private Object target;
	private Method method;
	private Render render;
	
	private List<Interceptor>interceptors=null;
	private int interceptIndex=0;
	
	public ActionInvocation(){
	}
	public ActionInvocation(Object target,Method method,List<Interceptor>interceptors){
		this.target=target;
		this.method=method;
		this.interceptors=interceptors;
	}
	
	public final void invoke() throws ActionInvocationException{
		if(interceptors!=null && interceptIndex<interceptors.size()){
			Interceptor calling=interceptors.get(interceptIndex);
			interceptIndex++;
			calling.intercept(this);
			return;
		}		
		try {
			method.invoke(target);
			if (render!=null) {
				render.render();
			}
		} catch (IllegalAccessException e) {
			throw new ActionInvocationException(e);
		} catch (IllegalArgumentException e) {
			throw new ActionInvocationException(e);
		} catch (InvocationTargetException e) {
			throw new ActionInvocationException(e);
		}
	}
	
	public List<Interceptor> getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Render getRender() {
		return render;
	}
	public void setRender(Render render) {
		this.render = render;
	}	
}
