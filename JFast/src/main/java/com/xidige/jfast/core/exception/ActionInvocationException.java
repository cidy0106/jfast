package com.xidige.jfast.core.exception;

/**
 * action调用发生异常
 * @author kime
 *
 */
public class ActionInvocationException extends UnHandleException {
	private static final long serialVersionUID = 587771840700661453L;
	public ActionInvocationException() {
		super();
	}
	public ActionInvocationException(String message) {
		super(message);
	}
	public ActionInvocationException(String message, Throwable cause) {
		super(message,cause);
	}
	public ActionInvocationException(Throwable cause) {
		super(cause);
	}
}
