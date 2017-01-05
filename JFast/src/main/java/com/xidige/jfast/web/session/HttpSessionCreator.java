package com.xidige.jfast.web.session;

import javax.servlet.http.HttpSession;

import com.xidige.jfast.web.RequestContext;

public interface HttpSessionCreator {
	public HttpSession doCreateSession(RequestContext requestContext,boolean created);
}
