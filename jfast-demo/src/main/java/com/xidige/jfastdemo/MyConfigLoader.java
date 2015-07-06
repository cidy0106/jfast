package com.xidige.jfastdemo;

import com.xidige.jfast.core.Config;
import com.xidige.jfast.core.ConfigLoader;
import com.xidige.jfast.core.exception.ConfigException;
import com.xidige.jfast.web.interceptor.ExceptionInterceptor;
import com.xidige.jfast.web.uri.DefaultUriParser;

public class MyConfigLoader implements ConfigLoader {

	@Override
	public void load(Config config) throws ConfigException {
		config.addInterceptor(new ExceptionInterceptor());
				
		config.addUriParser(new DefaultUriParser());
		
		config.addActionLookup(new MyActionLookup());
	}

}
