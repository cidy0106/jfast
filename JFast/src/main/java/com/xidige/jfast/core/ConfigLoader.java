package com.xidige.jfast.core;

import com.xidige.jfast.core.exception.ConfigException;


/**
 * 配置加载接口
 * 
 * @author kime
 *
 */
public interface ConfigLoader {
	/**
	 * 设置jfconfig
	 * @param config
	 */
	public void load(Config config)throws ConfigException;
}
