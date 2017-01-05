package com.xidige.jfastdemo.session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 用sessionid+作为key保存是session数据
 * @author kime
 *
 */

import com.xidige.jfast.web.RequestContext;

import redis.clients.jedis.ShardedJedis;

public class JedisHttpSession implements HttpSession {
	private static Log LOG = LogFactory.getLog(JedisHttpSession.class);

	/**
	 * session放到jedis里的key前缀，后面跟sessionid
	 */
	private static final String PREFIX_SESSIONID="__sesId.";
	/**
	 * 序列化时使用
	 */
	private static final String SERIALIZE_CHARSETNAME="ISO-8859-1";
	/**
	 * 默认有效期间隔，秒
	 */
	private static final int EXPIRE_DEFAULT_SECONDS=1800;
	/**
	 * 保存在jedis的key
	 */
	private String jedisSessionId;
	
	private String sessionId;
	private RequestContext requestContext;

	private long creationTime;
	private long lastAccessedTime;
	private int maxInactiveInterval = 0;
	private boolean isNew = true;// 如果是从redis取出的，就是false
	
	public JedisHttpSession(RequestContext requestContext, String sessionId) {
		this.sessionId = sessionId;
		this.jedisSessionId=PREFIX_SESSIONID+sessionId;
		this.requestContext = requestContext;
		init();
	}
	
	/**
	 * 从redis初始化session
	 * 读取creationTime，
	 * 设置属性值，
	 * 更新有效期
	 */
	private void init(){
		ShardedJedis jedis = JedisClientManager.openRedisClient();
		if(jedis.exists(jedisSessionId)){
			this.isNew=false;
			String tempStr=jedis.hget(jedisSessionId, "__creationTime");
			this.creationTime=tempStr==null?System.currentTimeMillis():Long.parseLong(tempStr);
			tempStr=jedis.hget(jedisSessionId, "__lastAccessedTime");
			this.lastAccessedTime=tempStr==null?System.currentTimeMillis():Long.parseLong(tempStr);
		}else{
			this.isNew=true;
			this.creationTime=System.currentTimeMillis();
			this.lastAccessedTime=this.creationTime;
			
			jedis.hset(jedisSessionId, "__creationTime", String.valueOf(this.creationTime));
		}
		//更新有效期等
		jedis.hset(jedisSessionId, "__lastAccessedTime", String.valueOf(System.currentTimeMillis()));
		if(maxInactiveInterval==0){
			jedis.expire(jedisSessionId, EXPIRE_DEFAULT_SECONDS);
		}else{
			jedis.expire(jedisSessionId, maxInactiveInterval);
		}
	}

	@Override
	public long getCreationTime() {
		return this.creationTime;
	}

	@Override
	public String getId() {
		return this.sessionId;
	}

	@Override
	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}
	
	public void setLastAccessedTime(long lastAccessedTime){
    	this.lastAccessedTime=lastAccessedTime;
    }

	@Override
	public ServletContext getServletContext() {
		return this.requestContext.getRequest().getServletContext();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}
	
	
	@Override
	public void invalidate() {
		ShardedJedis jedis = JedisClientManager.openRedisClient();
		jedis.del(jedisSessionId);
	}

	/**
	 * 如果是从redis取出的，就是false
	 */
	@Override
	public boolean isNew() {
		return  this.isNew;
	}
	
	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		ShardedJedis jedis = JedisClientManager.openRedisClient();
		return new Vector<String>(jedis.hkeys(jedisSessionId)).elements();
	}

	@Override
	public String[] getValueNames() {
		ShardedJedis jedis = JedisClientManager.openRedisClient();
		return (String[]) jedis.hkeys(jedisSessionId).toArray();
	}
	
	private String serialize(Object object) throws IOException{
		ByteArrayOutputStream buffer=new ByteArrayOutputStream();
		ObjectOutputStream outputStream=new ObjectOutputStream(buffer);
		outputStream.writeObject(object);
		return buffer.toString(SERIALIZE_CHARSETNAME);
	}
	
	private Object unSerialize(String data) throws IOException, ClassNotFoundException{
		ByteArrayInputStream buffer=new ByteArrayInputStream(data.getBytes(SERIALIZE_CHARSETNAME));
		ObjectInputStream inputStream=new ObjectInputStream(buffer);
		return inputStream.readObject();
	}
	
	@Override
	public void removeValue(String name) {
		if(name==null){
			return;
		}
		ShardedJedis jedis = JedisClientManager.openRedisClient();
		jedis.hdel(jedisSessionId, name);
	}
	
	@Override
	public Object getValue(String name) {
		ShardedJedis jedis = JedisClientManager.openRedisClient();
		String data=jedis.hget(jedisSessionId, name);
		if(data==null){
			return null;
		}
		try {
			return unSerialize(data);
		} catch (ClassNotFoundException e) {
			LOG.error("getValue", e);
		} catch (IOException e) {
			LOG.error("getValue", e);
		}
		return null;
	}
	
	@Override
	public void putValue(String name, Object value) {
    	if (value==null) {
			this.removeValue(name);
			return;
    	}
    	try {
    		String seValue=serialize(value);
    		ShardedJedis jedis = JedisClientManager.openRedisClient();
			jedis.hset(jedisSessionId, name, seValue);
		} catch (IOException e) {
			LOG.error("putValue", e);
		}
	}
	
	@Override
	public Object getAttribute(String name) {
		return getValue(name);
	}
	
	@Override
	public void setAttribute(String name, Object value) {
		if(value!=null && value instanceof HttpSessionBindingListener) {
			((HttpSessionBindingListener) value).valueBound(new HttpSessionBindingEvent(this,name,value));
		}
    	this.putValue(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		Object obj=getAttribute(name);
    	if (obj instanceof HttpSessionBindingListener) {
			((HttpSessionBindingListener) obj).valueUnbound(new HttpSessionBindingEvent(this,name,obj));
		}
    	this.removeValue(name);
	}

	
	
	

}
