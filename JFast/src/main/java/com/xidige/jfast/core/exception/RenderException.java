package com.xidige.jfast.core.exception;


/**
 * 渲染异常
 * @author kime
 *
 */
public class RenderException extends UnHandleException {
	private static final long serialVersionUID = 1061528545532735906L;
	public RenderException() {
		super();
	}
	public RenderException(String message) {
		super(message);
	}
	public RenderException(String message, Throwable cause) {
		super(message,cause);
	}
	public RenderException(Throwable cause) {
		super(cause);
	}
}
