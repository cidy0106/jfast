package com.xidige.jfastdemo.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.xidige.jfast.core.log.Log;
import com.xidige.jfast.core.log.LogFactory;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class JedisClientManager {
	private static final Log LOG=LogFactory.getLog(JedisClientManager.class);
	
	private Properties properties_config;
	private ShardedJedisPool redisPool;

	private JedisClientManager() {
		properties_config = new Properties();
		try {
			properties_config.load(JedisClientManager.class.getResourceAsStream("/jredis.properties"));
		} catch (IOException e) {
			LOG.error("JedisClientManager can't load jredis.properties");
			throw new RuntimeException(e);
		}

		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000 * 100);
		config.setTestOnBorrow(true);

		String host = properties_config.getProperty("redis.host", "127.0.0.1");
		String password = properties_config.getProperty("redis.password");
		int port = Integer.parseInt(properties_config.getProperty("redis.port", "6379"));
//		int timeout = Integer.parseInt(properties_config.getProperty("redis.timeout", "100000"));
		boolean ssl = Boolean.parseBoolean(properties_config.getProperty("redis.ssl", "false"));

		String uri = (ssl ? "rediss" : "redis") + "://" + (StringUtils.isNotEmpty(password) ? ":" + password + "@" : "")
				+ host + ":" + port;
		// slave链接
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();

		shards.add(new JedisShardInfo(uri));

		// 构造池
		redisPool = new ShardedJedisPool(config, shards);
	}

	public static ShardedJedis openRedisClient() {
		return Holder.INSTANCE.redisPool.getResource();
	}

	private static class Holder {
		private static JedisClientManager INSTANCE = new JedisClientManager();
	}

}
