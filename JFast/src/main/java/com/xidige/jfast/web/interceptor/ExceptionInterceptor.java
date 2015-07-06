package com.xidige.jfast.web.interceptor;

import com.xidige.jfast.core.ActionProxy;
import com.xidige.jfast.core.Interceptor;

public class ExceptionInterceptor implements Interceptor {

	public ExceptionInterceptor() {
		// TODO Auto-generated constructor stub
	}

	public void intercept(ActionProxy actionProxy) {
		try {
			actionProxy.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
