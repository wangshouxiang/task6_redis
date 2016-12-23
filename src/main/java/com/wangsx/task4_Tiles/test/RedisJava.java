package com.wangsx.task4_Tiles.test;


import java.util.List;

import com.wangsx.task4_Tiles.model.Student;

import redis.clients.jedis.Jedis;

public class RedisJava {
	private static Jedis jedis;

	public static void main(String[] args) {
		/*//连接本地的 Redis 服务
	      Jedis jedis = new Jedis("localhost");
	      System.out.println("Connection to server sucessfully");
	      //存储数据到列表中
	      jedis.rpush("tutorial-list4", "Redis");
	      jedis.rpush("tutorial-list4", "Mongodb");
	      jedis.rpush("tutorial-list4", "Mysql");
	     // 获取存储的数据并输出
	     List<String> list = jedis.lrange("tutorial-list4", 0 ,3);
	     for(int i=0; i<list.size(); i++) {
	       System.out.println("Stored string in redis:: "+list.get(i));
	     }*/
	  System.out.println((Student)null);
	}

	
}
