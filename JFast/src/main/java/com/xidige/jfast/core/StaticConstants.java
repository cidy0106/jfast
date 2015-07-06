package com.xidige.jfast.core;
/**
 * 系统常量
 * 
 * 比如：配置项名 等等
 * @author kime
 *
 */
public interface StaticConstants {
	/**
	 * 请求的编码
	 */
	public final static String JF_REQUEST_CHARACTER_ENCODING="jf.request.charset.encoding";
	/**
	 * 是否强制设置编码
	 */
	public final static String JF_REQUEST_CHARACTER_ENCODING_FORCE="jf.request.charset.encoding.force";
	/**
	 * web根目录绝对路径
	 */
	public final static String JF_ROOL_REAL_PATH="jf.root.real.path";
	/**
	 * web应用目录，比如：/jfast
	 */
	public final static String JF_CONTEXT_PATH="jf.context.path";
	/**
	 * ServletContext
	 */
	public final static String JF_SERVLET_CONTEXT="jf.servlet.context";
	/**
	 * 初始参数
	 * @author kime
	 *
	 */
	public static interface INITPARAMS{
		/**
		 * 配置加载类
		 */
		public final static String JF_CONFIG_FACTORY="jf.param.init.configfactory";
	}
	
}
