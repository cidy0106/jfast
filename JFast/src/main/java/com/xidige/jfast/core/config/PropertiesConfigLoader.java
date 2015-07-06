package com.xidige.jfast.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import com.xidige.jfast.core.Config;
import com.xidige.jfast.core.ConfigLoader;
import com.xidige.jfast.core.Interceptor;
import com.xidige.jfast.core.UriParser;
import com.xidige.jfast.core.exception.ConfigException;
import com.xidige.jfast.core.log.Log;
import com.xidige.jfast.core.log.LogFactory;
/**
 * 从classes里加载配置文件 
 * 默认为： jfast.properties
 * 
 * 把配置加载到attrs里,全部转成字符串进行保存
 
 * @author kime
 *
 *
 *
 */
public class PropertiesConfigLoader implements ConfigLoader {
	private static Log log=LogFactory.getLog(PropertiesConfigLoader.class);
	
	private String propertiesFile=null;
	public PropertiesConfigLoader(String propertiesFile) {
		if (propertiesFile==null || "".equals(propertiesFile)) {
			this.propertiesFile="jfast.properties";
		}else {
			this.propertiesFile=propertiesFile;
		}		
	}

	public void load(Config config)throws ConfigException {
		Properties properties=new Properties();
		InputStream inputStream=null;
		try {
			inputStream=this.getClass().getResourceAsStream(this.propertiesFile);
			properties.load(inputStream);
			String name=null;
			for (Iterator<Entry<Object, Object>>  iterator =  properties.entrySet().iterator(); iterator.hasNext();) {
				Entry<Object, Object> entry = iterator.next();
				name=String.valueOf(entry.getKey());
				if (! transform(config,name,entry.getValue())) {
					config.setAttr(name, String.valueOf(entry.getValue()));
				}				
			}
		} catch (IOException e) {
			throw new ConfigException(e);
		}finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			properties.clear();
		}		
	}
	/**
	 * 转换参数，
	 * 如果参数名是需要转换的对象，不管转换成功与否都返回true
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	protected boolean transform(Config config,String name,Object value){
		if ("interceptor".equals(name)) {
			//拦截器，可以多个，用逗号分割
			String tempStr=String.valueOf(value);
			String[]itcStrs=tempStr.split("\\,");
			int length=itcStrs.length;
			Interceptor interceptor=null;
			for (int i = 0; i < length; i++) {
				interceptor=getInterceptorByStr(itcStrs[i]);
				if (interceptor!=null) {
					config.addInterceptor(interceptor);
				}
			}
			return true;
		}else if ("uriparser".equals(name)) {
			//多个，用逗号分割
			String tempStr=String.valueOf(value);
			String[]itcStrs=tempStr.split("\\,");
			int length=itcStrs.length;
			UriParser uriParser=null;
			for (int i = 0; i < length; i++) {
				uriParser=getUriParserByStr(itcStrs[i]);
				if (uriParser!=null) {
					config.addUriParser(uriParser);
				}
			}
			return true;
		}
		
		return false;
	}
	private UriParser getUriParserByStr(String classStr) {
		try {
			Class<?>clz= Class.forName(classStr);
			return (UriParser) clz.newInstance();
		} catch (Exception e) {
			log.error("getUriParserByStr", e);
		}
		return null;
	}

	private Interceptor getInterceptorByStr(String classStr){
		try {
			Class<?>clz= Class.forName(classStr);
			return (Interceptor) clz.newInstance();
		} catch (Exception e) {
			log.error("getInterceptorByStr", e);
		}
		return null;
	}
}
