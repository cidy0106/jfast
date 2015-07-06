package com.xidige.jfast.core.db;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * 数据源
 * 1,内部使用自己使用的连接池工具类,并返回动态代理的connection,以便修改链接的关闭功能;
 * 2,内部使用localthread来尽量为当前线程维持一个connection;
 * 3,提供一个清除当前线程变量的方法,外部调用关闭时把链接放置到连接池中
 * 
 * @author kime
 *
 */
public class MyDataSource implements DataSource{
	private final static ThreadLocal<Connection>connectionLocal=new ThreadLocal<Connection>();

	/**
	 * 初始化与当前线程相关的链接
	 * 如果没有就从连接池取出链接关联到本线程
	 */
	public static Connection currentConnection(){
		Connection currentConnection=connectionLocal.get();
		if (currentConnection==null) {
			currentConnection=DBConnectionPool.openConnection();
			if (currentConnection!=null) {
				currentConnection=MyConnectionProxy.getConnectionProxy(currentConnection);
				connectionLocal.set(currentConnection);
			}			
		}
		return currentConnection;
	}
	/**
	 * 把与本线程关联的链接放回连接池中
	 */
	public static void closeConnection(){
		Connection currentConnection=connectionLocal.get();
		if (currentConnection!=null) {
			connectionLocal.set(null);
			DBConnectionPool.closeConnection(currentConnection);
		}
	}
	/**
	 * 拦截关闭方法,不让其真正关闭,真正的关闭,将由closeSession另外处理
	 * @author kime
	 *
	 */
	private static class MyConnectionProxy implements InvocationHandler{
		private Connection origion;
		public MyConnectionProxy(Connection connection){
			this.origion=connection;
		}
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if (method.getName().equals("close")) {
//				DBConnectionPool.closeConnection(origion);
				return null;
			}else {
				return method.invoke(origion, args);
			}
		}
		/**
		 * 创建一个代理connection
		 * @param connection
		 * @return
		 */
		public static Connection getConnectionProxy(Connection connection){
			Object proxy=Proxy.newProxyInstance(connection.getClass().getClassLoader(), new Class[]{Connection.class},new MyConnectionProxy(connection));
			return (Connection) proxy;
		}
	}
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	public Connection getConnection() throws SQLException {
		return currentConnection();
	}
	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
