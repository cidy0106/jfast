package com.xidige.jfastdemo.web.action;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xidige.jfast.core.log.Log;
import com.xidige.jfast.core.log.LogFactory;
import com.xidige.jfast.web.RequestContext;

public class Hello {
	private static Log log=LogFactory.getLog(Hello.class);
	
	private static final String MESSAGE_STRING="Hi,i am kime,who are you~";
	private static final String TEMPLATE_PATH="/WEB-INF/jsp/";
	
	public void index() throws IOException, ServletException{
		RequestContext requestContext = RequestContext.getContext();
//		requestContext.getResponse().getWriter().append("index....").flush();
		HttpServletRequest request=requestContext.getRequest();
		HttpSession session=request.getSession(false);
		if(session!=null){
			String user=(String) session.getAttribute("user");
			if(user!=null){
				requestContext.redirect(TEMPLATE_PATH+"hello.jsp");
				return;
			}
		}
		requestContext.redirect(TEMPLATE_PATH+"loginForm.jsp");
	}
	
	public void login() throws IOException{
		RequestContext requestContext = RequestContext.getContext();
		HttpServletRequest request=requestContext.getRequest();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		if("admin".equals(username) && "admin".equals(password)){
			HttpSession session=request.getSession(true);
			session.setAttribute("user", "admin");
		}
//		requestContext.getResponse().getWriter().append("{\"error\":\"false\"}").flush();
		HttpServletResponse response=requestContext.getResponse();
		response.sendRedirect("index");
	}
	
	
	public void sayHi(){
		//to console
		System.out.println(MESSAGE_STRING);
		RequestContext requestContext = RequestContext.getContext();
		HttpServletRequest req= requestContext.getRequest();
		Enumeration<String>en= req.getParameterNames();
		if(en!=null){
			while (en.hasMoreElements()) {
				String name = en.nextElement();
				System.out.println(name+"="+req.getParameter(name));
			} 
		}
		
	}
	
	/**
	 * 登录表单
	 */
	public void sayHtml(){
		//to html
		RequestContext requestContext = RequestContext.getContext();
		try {
//			requestContext.getResponse().getWriter().write(MESSAGE_STRING);
			HttpServletRequest request=requestContext.getRequest();
			String username=request.getParameter("username");
			request.setAttribute("reply", username+",你好啊");
			requestContext.redirect(TEMPLATE_PATH+"loginForm.jsp");
		} catch (IOException | ServletException e) {
			log.error("sayHtml error", e);
		}				
	}
	public void testSession(){
		RequestContext requestContext = RequestContext.getContext();
		HttpServletRequest request=requestContext.getRequest();
		HttpSession session=request.getSession(true);
		session.setAttribute("test", "kime");
		System.out.println(session.getAttribute("test"));
	}
}
