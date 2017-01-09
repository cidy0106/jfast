package com.xidige.jfastdemo.session;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.xidige.jfast.web.RequestContext;
import com.xidige.jfast.web.session.HttpSessionCreator;

public class JedisSessionCreator implements HttpSessionCreator {
	private final static int MAX_ENTRY=10000;
	private final static int MAX_INACTIVE_INTERVAL=1800;//秒
	private static Map<String, JedisHttpSession>sessionCached=new LinkedHashMap<String, JedisHttpSession>(){
		protected boolean removeEldestEntry(Map.Entry<String,JedisHttpSession> eldest) {
			return size()>MAX_ENTRY;
		};
	};
	
	@Override
	public HttpSession doCreateSession(boolean created) {
		RequestContext requestContext=RequestContext.getContext();
		HttpServletRequest request=requestContext.getRequest();
		HttpServletResponse response=requestContext.getResponse();
		String sessionId=request.getRequestedSessionId();
		if (sessionId==null) {
			sessionId=UUID.randomUUID().toString();
			updateSessionCookie(request,response,sessionId);
		}else{
			//如果是页面的sessionid，判断一下格式,我这里用的是uuid做为sessionid
			try {
				UUID.fromString(sessionId);
			} catch (IllegalArgumentException e) {
				//新建一个
				sessionId=UUID.randomUUID().toString();
				updateSessionCookie(request,response,sessionId);
			}
		}
		JedisHttpSession session=sessionCached.get(sessionId);
		if (session!=null) {
			return session;
		}else{
			session = new JedisHttpSession(requestContext, sessionId);
			sessionCached.put(sessionId, session);
			session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
		}
		
		return session;
	}
	
	/**
	 * 更新session cookie的信息
	 * @param request
	 * @param response
	 * @param session
	 */
	protected void updateSessionCookie(HttpServletRequest request,HttpServletResponse response,String sessionId){
		SessionCookieConfig cookieConfig= request.getServletContext().getSessionCookieConfig();
		String cookieName=cookieConfig.getName();
		if(StringUtils.isEmpty(cookieName)){
			cookieName="JSESSIONID";
		}
		Cookie cookie = new Cookie(cookieName, sessionId);
		cookie.setComment(cookieConfig.getComment());
		cookie.setPath(cookieConfig.getPath()); 
		cookie.setMaxAge(cookieConfig.getMaxAge());
		cookie.setHttpOnly(cookieConfig.isHttpOnly());
		response.addCookie(cookie);
	}
}
