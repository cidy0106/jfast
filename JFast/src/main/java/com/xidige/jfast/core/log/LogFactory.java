package com.xidige.jfast.core.log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 简单实现，嫁接到apache 的common logging
 * @author kime
 *
 */
public class LogFactory {
	private static Class<?> factoryClass=null;
	private static Log defaultSelfLog=new SimpleLog(LogFactory.class);
	static{
		try {
			factoryClass=Class.forName("org.apache.commons.logging.LogFactory");
		} catch (Exception e) {
			defaultSelfLog.debug("org.apache.commons.logging.LogFactory not found");
		}
	}
	public static Log getLog(String className){
		if (factoryClass!=null) {
			try {
				Method getLogMethod=factoryClass.getMethod("getLog", String.class);
				if (getLogMethod!=null) {
					return (Log) getLogMethod.invoke(null, className);
				}
			} catch (NoSuchMethodException e) {
				defaultSelfLog.debug("getLog", e);
			} catch (IllegalAccessException e) {
				defaultSelfLog.debug("getLog", e);
			} catch (IllegalArgumentException e) {
				defaultSelfLog.debug("getLog", e);
			} catch (InvocationTargetException e) {
				defaultSelfLog.debug("getLog", e);
			}
		}else {
			return new SimpleLog(className);
		}
		return null;
	}
	public static Log getLog(Class<?>clz){
		return getLog(clz.getCanonicalName());
	}
	
	private static class SimpleLog implements Log{
		private String clz=null;
		public SimpleLog(String clz){
			this.clz=clz;
		}
		public SimpleLog(Class<?>claz){
			this(claz.getCanonicalName());
		}
		@Override
		public void debug(String message) {
			System.out.println("[DEBUG "+clz+"] \n"+message);
		}

		@Override
		public void debug(String message, Throwable t) {
			debug(message+"\n( Caused by \n"+t.toString()+")");
		}

		@Override
		public void error(String message) {
			System.err.println("[ERROR "+clz+"] \n"+message);
		}

		@Override
		public void error(String message, Throwable t) {
			error(message+"\n( Caused by \n"+t.toString()+")");
		}

		@Override
		public void fatal(String message) {
			System.err.println("[FATAL "+clz+"] \n"+message);
		}

		@Override
		public void fatal(String message, Throwable t) {
			fatal(message+"\n( Caused by \n"+t.toString()+")");
		}

		@Override
		public void info(String message) {
			System.out.println("[INFO "+clz+"] \n"+message);
		}

		@Override
		public void info(String message, Throwable t) {
			info(message+"\n( Caused by \n"+t.toString()+")");
		}

		@Override
		public void trace(String message) {
			System.out.println("[TRACE "+clz+"] \n"+message);
		}

		@Override
		public void trace(String message, Throwable t) {
			trace(message+"\n( Caused by \n"+t.toString()+")");
		}

		@Override
		public void warn(String message) {
			System.out.println("[WARN "+clz+"] \n"+message);
		}

		@Override
		public void warn(String message, Throwable t) {
			warn(message+"\n( Caused by \n"+t.toString()+")");
		}

		@Override
		public boolean isDebugEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isErrorEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isFatalEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isInfoEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isTraceEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isWarnEnabled() {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
}
