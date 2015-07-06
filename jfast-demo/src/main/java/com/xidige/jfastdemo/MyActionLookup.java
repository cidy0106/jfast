package com.xidige.jfastdemo;

import com.xidige.jfast.core.ActionInvocation;
import com.xidige.jfast.core.ActionLookup;
import com.xidige.jfast.core.ActionManager;
import com.xidige.jfast.core.UriInfo;
import com.xidige.jfast.core.log.Log;
import com.xidige.jfast.core.log.LogFactory;
import com.xidige.jfast.core.util.StringUtil;

public class MyActionLookup implements ActionLookup {
	private static Log log=LogFactory.getLog(MyActionLookup.class);
	
	private final static String packageName="com.xidige.jfastdemo.web.action";
	private final static String methodName="index";
	
	private static ActionManager actionManager=ActionManager.getInstance();
	
	@Override
	public ActionInvocation lookup(UriInfo uriInfo) {		
		Object action=	actionManager.findAction(packageName+"."+StringUtil.capitalize(uriInfo.getActionName()));
		if (action!=null) {
			ActionInvocation actionInvocation=new ActionInvocation();
			actionInvocation.setTarget(action);
			if (uriInfo.getMethodName()==null || uriInfo.getMethodName().length()==0) {
				actionInvocation.setMethod(actionManager.findMethod(action, methodName));				
			}else{
				actionInvocation.setMethod(actionManager.findMethod(action, uriInfo.getMethodName()));
			}
			
			
			return actionInvocation;
		}
		return null;
	}

}
