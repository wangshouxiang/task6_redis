package com.wangsx.task4_Tiles.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import com.wangsx.task4_Tiles.dao.*;
import com.wangsx.task4_Tiles.model.*;
import com.wangsx.task4_Tiles.util.MemcachedKeyUtil;
import com.wangsx.task4_Tiles.util.RedisUtil;
import com.wangsx.task4_Tiles.util.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class ClassesServiceImpl extends MemcachedSupport implements ClassesService {
	@Autowired
	ClassesMapper classesMapper;
	Jedis jedis;
	JedisPool jedisPool = RedisUtil.getInstancePool();
	public Classes selectClassesById(int id) {
		jedis = jedisPool.getResource();
		Classes classes = null;
		String classesKey = MemcachedKeyUtil.getClassesKey(id);
		try {
			byte[] value = jedis.get(classesKey.getBytes());
			classes = (Classes)SerializeUtil.unserialize(value);
			if(classes == null) {
				classes = classesMapper.selectClassesById(id);
				if(classes !=null) {
					jedis.set(classesKey.getBytes(),SerializeUtil.serialize(classes));
					System.out.println("Redishuancun-----------------"+classesKey+ jedis.get(classesKey.getBytes()) + "-----------------");
				}
			}
		}finally {
			jedisPool.returnResource(jedis);
		}
		return classes;
	}

	@Override
	public void insertClasses(Classes classes) {
		jedis = jedisPool.getResource();
		classesMapper.insertClasses(classes);
		try{
			String classesKey = MemcachedKeyUtil.getClassesKey(classes.getClassId());
			jedis.set(classesKey.getBytes(),SerializeUtil.serialize(classes));
			System.out.println("Redishuancun-----------------"+classesKey+ jedis.get(classesKey.getBytes()) + "-----------------");
		}finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public int findLearningCount(int professionId) {
		
		return classesMapper.findLearningCount(professionId);
	}

}
