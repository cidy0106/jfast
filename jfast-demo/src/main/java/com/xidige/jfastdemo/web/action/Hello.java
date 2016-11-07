package com.xidige.jfastdemo.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.xidige.jfast.core.log.Log;
import com.xidige.jfast.core.log.LogFactory;
import com.xidige.jfast.web.RequestContext;

public class Hello {
	private static Log log=LogFactory.getLog(Hello.class);
	
	private static final String MESSAGE_STRING="Hi,i am kime,who are you~";
	private static final String TEMPLATE_PATH="/WEB-INF/jsp/";
	
	public void index() throws IOException{
		RequestContext requestContext = RequestContext.getContext();
		requestContext.getResponse().getWriter().append("index....").flush();
	}
	public void sayHi(){
		//to console
		System.out.println(MESSAGE_STRING);		
	}
	public void sayHtml(){
		//to html
		RequestContext requestContext = RequestContext.getContext();
		try {
//			requestContext.getResponse().getWriter().write(MESSAGE_STRING);
			HttpServletRequest request=requestContext.getRequest();
			String username=request.getParameter("username");
			request.setAttribute("reply", username+",你好啊");
			requestContext.redirect(TEMPLATE_PATH+"hello.jsp");
		} catch (IOException | ServletException e) {
			log.error("sayHtml error", e);
		}				
	}
}
