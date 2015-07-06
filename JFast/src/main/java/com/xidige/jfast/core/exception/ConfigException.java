package com.xidige.jfast.core.exception;

public class ConfigException extends UnHandleException {
	private static final long serialVersionUID = -5255626934397488632L;
	public ConfigException() {
		super();
	}
	public ConfigException(String message) {
		super(message);
	}
	public ConfigException(String message, Throwable cause) {
		super(message,cause);
	}
	public ConfigException(Throwable cause) {
		super(cause);
	}
}
