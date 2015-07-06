package com.xidige.jfast.core;

import com.xidige.jfast.core.exception.ActionInvocationException;
/**
 * action代理
 * @author kime
 *
 */
public interface ActionProxy {
	public void invoke() throws ActionInvocationException;
}
