package com.xidige.jfast.core;


/**
 * 拦截器
 * 调用action前后会调用 拦截器
 * @author kime
 *
 */
public interface Interceptor {
	/**
	 * 如果要继续后续处理,记得调用actionProxy的invoke方法
	 * @param actionProxy
	 */
	public void intercept(ActionProxy actionProxy);
}
