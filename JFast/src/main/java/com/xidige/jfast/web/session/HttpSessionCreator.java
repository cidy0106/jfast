package com.xidige.jfast.web.session;

import javax.servlet.http.HttpSession;

public interface HttpSessionCreator {
	public HttpSession doCreateSession(boolean created);
}
