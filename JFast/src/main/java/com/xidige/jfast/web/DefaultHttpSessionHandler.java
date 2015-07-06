package com.xidige.jfast.web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.http.HttpSession;
/**
 * session实现 * 
 * 在内存紧张时，把内存数据转存到硬盘等外部存储设备，主要是使用第三方缓存组件实现（比如ehcache）
 * 
 * 实现思路：用代理拦截特定方法的调用，把session内的attribute存储转向第三方缓存
 * 
 * @author kime
 *
 *!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!暂未实现!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 */
public class DefaultHttpSessionHandler implements InvocationHandler {
	private HttpSession httpSession;
	private DefaultHttpSessionHandler(HttpSession httpSession){
		this.httpSession=httpSession;
		throw new RuntimeException("暂未实现");
	}
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if("setAttribute".equals(method.getName())){
//			String id=httpSession.getId();
			
		}else if ("getAttribute".equals(method.getName())) {
						
		}
		return method.invoke(httpSession, args);
	}

	public static HttpSession wrapSession(HttpSession session){
		if (session==null) {
			return null;
		}
		return (HttpSession) Proxy.newProxyInstance(session.getClass().getClassLoader(),
				session.getClass().getInterfaces(), new DefaultHttpSessionHandler(session));
	}
}
