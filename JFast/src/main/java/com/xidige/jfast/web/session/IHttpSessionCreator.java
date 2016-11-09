package com.xidige.jfast.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface IHttpSessionCreator {
	public HttpSession doCreateSession(HttpServletRequest request,boolean created);
}
