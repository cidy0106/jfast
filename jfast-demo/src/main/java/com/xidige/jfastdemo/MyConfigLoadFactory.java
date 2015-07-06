package com.xidige.jfastdemo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterConfig;

import com.xidige.jfast.core.ConfigFactory;
import com.xidige.jfast.core.ConfigLoader;
import com.xidige.jfast.core.config.FilterConfigLoader;

public class MyConfigLoadFactory implements ConfigFactory {
//	private static Log
	@Override
	public List<ConfigLoader> getConfigLoaders(FilterConfig filterConfig) {
		List<ConfigLoader>loaders=new ArrayList<ConfigLoader>();
		loaders.add(new FilterConfigLoader(filterConfig));
//		loaders.add(new PropertiesConfigLoader(null));
		loaders.add(new MyConfigLoader());
		return loaders;
	}

}
