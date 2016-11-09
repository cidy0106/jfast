package com.xidige.jfast.core;

import java.util.List;
import java.util.Map;

public class UriInfo {
	private String actionName;
	private String methodName;
	private Map<String, List<String>>parameters;
	
	public Map<String, List<String>> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, List<String>> parameters) {
		this.parameters = parameters;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}	
}
