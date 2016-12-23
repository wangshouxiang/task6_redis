package com.wangsx.task4_Tiles.util;

import redis.clients.jedis.*;

public class RedisUtil {
	static JedisPool jedisPool;
    public  static JedisPool getInstancePool() {
    	 jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
    	 return jedisPool;
		
    }
}
