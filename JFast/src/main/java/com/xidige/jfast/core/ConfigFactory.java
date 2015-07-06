package com.xidige.jfast.core;

import java.util.List;

import javax.servlet.FilterConfig;

public interface ConfigFactory {
	public List<ConfigLoader> getConfigLoaders(FilterConfig filterConfig);
}
