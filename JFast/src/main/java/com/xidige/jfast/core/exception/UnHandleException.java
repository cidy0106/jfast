package com.xidige.jfast.core.exception;
/**
 * 不用捕获,或者无法捕获的异常,这部分异常,开发人员不需要捕获
 * 
 * 系统框架会统一捕获
 * @author kime
 *
 */
public class UnHandleException extends RuntimeException {
	protected Throwable cause = null;
	public UnHandleException() {
		super();
	}

	public UnHandleException(String message) {
		super(message);
	}
	public UnHandleException(String message, Throwable cause) {
		super(message+" ( Caused by "+cause+" )");
		this.cause=cause;
	}
	public UnHandleException(Throwable cause) {
		this(cause==null?null:cause.toString(),cause);
	}
	@Override
	public Throwable getCause() {
		return this.cause;
	}
}
