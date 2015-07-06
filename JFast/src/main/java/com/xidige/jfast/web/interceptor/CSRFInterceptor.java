package com.xidige.jfast.web.interceptor;

import com.xidige.jfast.core.ActionProxy;
import com.xidige.jfast.core.Interceptor;

public class CSRFInterceptor implements Interceptor {

	public CSRFInterceptor() {
		// TODO Auto-generated constructor stub
	}

	public void intercept(ActionProxy actionProxy) {
		actionProxy.invoke();
	}

	
	/**
	 * 默认做低级别的检查:post,referer;
	 * 高级别:post,referer,token;
	 * @param request
	 * @param checkCSRF
	 * @return
	 */
//	private boolean cleanCSRF(HttpServletRequest request,CheckCSRF checkCSRF){		
//		if (checkCSRF.level() == LEVEL.HIGHT) {
//			return "POST".equals(request.getMethod())
//					&& request.getServerName().endsWith(rootDomain) /**是否当前域名下*/
//					&& request.getParameter("ct")!=null 
//					&& CsrfToken.popCsrfToken(request.getParameter("ct")); /**是否有一次性令牌,参数名为:ct*/
//		}else if(checkCSRF.level() == LEVEL.MIDDLE){
//			return "POST".equals(request.getMethod())
//					&& request.getServerName().endsWith(rootDomain); /**是否当前域名下*/
//		}else {
//			return request.getServerName().endsWith(rootDomain); /**是否当前域名下*/
//		}
//	}
}
