package com.wangsx.task4_Tiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.wangsx.task4_Tiles.dao.ProfessionMapper;
import com.wangsx.task4_Tiles.model.Classes;
import com.wangsx.task4_Tiles.model.Profession;
import com.wangsx.task4_Tiles.util.MemcachedKeyUtil;
import com.wangsx.task4_Tiles.util.RedisUtil;
import com.wangsx.task4_Tiles.util.SerializeUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class ProfessionServiceImpl extends MemcachedSupport implements ProfessionService {
   
	@Autowired
	ProfessionMapper professionMapper;
	Jedis jedis; 
	JedisPool jedisPool = RedisUtil.getInstancePool();
	
	
	@Override
	public void insertProfession(Profession profession) {
		jedis = jedisPool.getResource();
		professionMapper.insertProfession(profession);
		try{
			String professionKey = MemcachedKeyUtil.getProfessionKey(profession.getProfessionId());
			jedis.set(professionKey.getBytes(),SerializeUtil.serialize(profession));
			System.out.println("Redishuancun-----------------"+professionKey+ jedis.get(professionKey.getBytes()) + "-----------------");
		}finally {
			jedisPool.returnResource(jedis);
		}
	}
	@Override
	public Profession selectProfessionById(int id) {
		jedis = jedisPool.getResource();
		Profession profession = null;
		String professionKey = MemcachedKeyUtil.getProfessionKey(id);
		try{
			byte[] value = jedis.get(professionKey.getBytes());
			profession = (Profession)SerializeUtil.unserialize(value);
			if(profession == null) {
				profession = professionMapper.selectProfessionById(id);
				if(profession != null) {
					jedis.set(professionKey.getBytes(),SerializeUtil.serialize(profession));
					System.out.println("Redishuancun-----------------"+professionKey+ jedis.get(professionKey.getBytes()) + "-----------------");
				}
			}
		}finally {
			jedisPool.returnResource(jedis);
		}
		return profession;
	}

	@Override
	public void deleteProfessionById(int id) {
		professionMapper.deleteProfessionById(id);
	}

	@Override
	public void updateProfessionById(Profession profession) {
		professionMapper.updateProfessionById(profession);
	}

}
