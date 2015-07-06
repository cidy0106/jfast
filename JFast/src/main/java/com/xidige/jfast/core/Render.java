package com.xidige.jfast.core;

import com.xidige.jfast.core.exception.RenderException;

/**
 * 渲染输出
 * @author kime
 *
 */
public interface Render {
	/**
	 * 调用该方法,即开始渲染
	 * @throws RenderException
	 */
	public void render()throws RenderException;
	
	
	
	public static enum CONTENT_TYPE{
		HTML("text/html"),JSON("text/javascript"),XML("text/xml"),PLAIN("text/plain"),STREAM("application/octet-stream");
		private String value=null;
		private CONTENT_TYPE(String contenttype){
			this.value=contenttype;
		}
		public String getValue(){
			return this.value;
		}
		@Override
		public String toString() {
			return this.value;
		}
	}
	public static enum VIEW_TYPE{
		STREAM,JSP,FREEMARKER,VELOCITY;
	}
}
