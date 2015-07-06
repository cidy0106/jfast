package com.xidige.jfast.core;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建action,并维护一个副本以供后续调用
 * 暂定用Map<String,SoftReference<Action>>保存引用
 * 
 * 
 * action类必须是具有无参构造函数,可实例化的,且是在特定包下;
 * method方法必须无参数,public;
 * 
 * 
 * @author kime
 *
 */
public class ActionManager {	
	private static ActionManager instance=null;
	private static Map<String, SoftReference<Object>>actions=new HashMap<String, SoftReference<Object>>();
	private static Map<String, SoftReference<Method>>methods=new HashMap<String, SoftReference<Method>>();
	private ActionManager(){
	}
	public synchronized static ActionManager getInstance(){
		if(instance==null){
			instance=new ActionManager();
		}
		return instance;
	}
	public Object findAction(String clz){
		Object action=null;
		if (!actions.containsKey(clz) || (action=actions.get(clz).get())==null) {
			try {
				action = Class.forName(clz).newInstance();
				synchronized (actions) {
					SoftReference<Object> actionRef = new SoftReference<Object>(
							action);
					actions.put(clz, actionRef);
				}
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
		return action;
	}
	/**
	 * 查找public的方法
	 * @param obj
	 * @param methodName
	 * @return
	 */
	public Method findMethod(Object obj,String methodName){
		String key=obj.getClass().getName()+"@"+methodName;
		Method method=null;
		if (!methods.containsKey(key) || (method=methods.get(key).get())==null) {
			try {
				method = obj.getClass().getMethod(methodName);
				if (method.getModifiers() == Modifier.PUBLIC) {
					synchronized (methods) {
						SoftReference<Method> methodRef = new SoftReference<Method>(
								method);
						methods.put(key, methodRef);
					}
				} else {
					method = null;// 不符合public的要求
				}
			} catch (NoSuchMethodException e) {
			} catch (SecurityException e) {
			}
		}
		return method;
	}
}
