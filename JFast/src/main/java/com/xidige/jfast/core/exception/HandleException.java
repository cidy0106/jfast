package com.xidige.jfast.core.exception;
/**
 * 需要开发人员主动捕获处理的异常
 * 
 * 如果开发人员没有捕获,最后框架也会统一捕获
 * @author kime
 *
 */
public class HandleException extends Exception {
	protected Throwable cause = null;
	public HandleException() {
		super();
	}

	public HandleException(String message) {
		super(message);
	}

	public HandleException(Throwable cause) {
		this(cause==null?null:cause.toString(),cause);
	}

	public HandleException(String message, Throwable cause) {
		super(message+" ( Caused by "+cause+" )");
		this.cause=cause;
	}
	@Override
	public Throwable getCause() {
		return this.cause;
	}
}
