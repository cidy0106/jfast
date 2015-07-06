package com.xidige.jfast.core.log;
/**
 * 这个接口直接复制了commons-logging的，不过，我需要的message是String的
 * 
 * @author kime
 *
 */
public interface Log {
    void debug(String message);
    void debug(String message, Throwable t);
    void error(String message);
    void error(String message, Throwable t);
    void fatal(String message);
    void fatal(String message, Throwable t);
    void info(String message);
    void info(String message, Throwable t);
    void trace(String message);
    void trace(String message, Throwable t);
    void warn(String message);
    void warn(String message, Throwable t);
    boolean isDebugEnabled();
    boolean isErrorEnabled();
    boolean isFatalEnabled();
    boolean isInfoEnabled();
    boolean isTraceEnabled();
    boolean isWarnEnabled();   
}
