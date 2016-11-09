package com.xidige.jfast.web.uri;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xidige.jfast.core.UriInfo;
import com.xidige.jfast.core.UriParser;
import com.xidige.jfast.core.log.Log;
import com.xidige.jfast.core.log.LogFactory;
import com.xidige.jfast.core.util.StringUtil;
import com.xidige.jfast.web.util.UriUtil;
/**
 * 指定包路径,和后缀
 * 内部解析到action的名称和方法名后,将拼接上包路径和后缀,比如:
 * com.xidige.jfast.action.名Action,然后尝试进行实例化为object;
 * 再根据方法名,反射出其内部方法;
 * 
 * 
 * @author kime
 *
 */
public class DefaultUriParser implements UriParser {
	private Log log=LogFactory.getLog(DefaultUriParser.class);	
	public DefaultUriParser() {
		// TODO Auto-generated constructor stub
	}

	public UriInfo parseUri(HttpServletRequest request) {
		String uri=UriUtil.getRequestUri(request);
		String encode=request.getCharacterEncoding();//编码
		 
		 
		if (StringUtil.isEmpty(uri) || "/".equals(uri)) {
			//首页,解析为indexAction的index方法
			UriInfo uriInfo=new UriInfo();
			
			uriInfo.setActionName("Index");//首字母大写
//			uriInfo.setMethodName("index");
			return uriInfo;
		}else {
			// 格式: /action/method/参/值/***/
			String actionName=null;
			String method=null;
			String uriInfos[] = StringUtil.split(uri, '/');
			
			//先解析出action和method
			int i=0;//索引
			//除非没有请求段了,否则取到第一个非空段作为action
			while(i<uriInfos.length && ((actionName=uriInfos[i++])==null|| "".equals(actionName))) {
				
			}			
			//除非没有请求段了,否则取到第一个非空段作为method
			while(i<uriInfos.length && ((method=uriInfos[i++])==null|| "".equals(method))) {
		
			}
			
			Map<String, List<String>>params=parseParameters(uriInfos, i,encode);			
			
			UriInfo uriInfo = new UriInfo();
			// action 只能是字母
			if (StringUtil.isAlpha(actionName)){
				actionName = StringUtil.capitalize(actionName);// 首字母大写
				uriInfo.setActionName(actionName);
				uriInfo.setMethodName(method);
				uriInfo.setParameters(params);
				
				return uriInfo;
			}			
			return null;
		}
	}	
	
	/**
	 * 记得检测 start的值,不能超过uriInfos的长度
	 * 
	 * 1,参数名不为空时,参数 值 可以为空
	 * 2,支持多值，uri上的参数优先，然后是默认的get，post参数
	 * @param uriInfos
	 * @param start
	 */
	protected Map<String, List<String>> parseParameters(String []uriInfos,int start,String encode){
		Map<String, List<String>>params =null;
		if (start<uriInfos.length) {
			params=new HashMap<String, List<String>>();
			String name = null, value = null;
			// /a/2/d/4/f,值都会解码
			for (int i=start; i < uriInfos.length;) {
				// 参数名不能为空
				while(i<uriInfos.length && ((name=uriInfos[i++])==null|| "".equals(name))) {
					
				}
				
				// 要获取参数值,可以为空
				if(i< uriInfos.length	) {
					value = uriInfos[i];
					if (!"".equals(value)) {
						//解码
						try {
							value=URLDecoder.decode(value, encode);
						} catch (UnsupportedEncodingException e) {
							value=null;
							log.error("parseUri", e);
						}
					}
				}
				i++;//下次循环
				
				List<String>vals=params.get(name);
				if (vals==null) {
					vals=new ArrayList<String>();
					params.put(name, vals);
				}
				vals.add(value);
			}
			
			return params;
		}else {
			return null;
		}		
	}
}
