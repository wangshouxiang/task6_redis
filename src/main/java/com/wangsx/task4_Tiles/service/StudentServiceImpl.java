package com.wangsx.task4_Tiles.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import com.wangsx.task4_Tiles.dao.*;
import com.wangsx.task4_Tiles.model.Student;
import com.wangsx.task4_Tiles.util.MemcachedKeyUtil;
import com.wangsx.task4_Tiles.util.RedisUtil;
import com.wangsx.task4_Tiles.util.SerializeUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class StudentServiceImpl extends MemcachedSupport implements StudentService {
	@Autowired
    StudentMapper studentMapper;
	Jedis jedis;
	JedisPool jedisPool = RedisUtil.getInstancePool();
	public Student selectStudentById(int id) {
		   jedis = jedisPool.getResource();
			Student student = null;
			String studentKey = MemcachedKeyUtil.getStudentsKey(id);
			try {
				byte[] value = jedis.get(studentKey.getBytes());
				student = (Student)SerializeUtil.unserialize(value);
				if(student == null) {
					student = studentMapper.selectStudentById(id);
					if(student != null) {
						jedis.set(studentKey.getBytes(),SerializeUtil.serialize(student));
						System.out.println("Redishuancun-----------------"+studentKey+ jedis.get(studentKey.getBytes()) + "-----------------");
					}
				}
			}finally {
				jedisPool.returnResource(jedis);
			}
		return student;
	}

	@Override
	public void insertStudent(Student student) {
		jedis = jedisPool.getResource();
		studentMapper.insertStudent(student);
		try {
			String studentKey = MemcachedKeyUtil.getStudentsKey(student.getId());
			jedis.set(studentKey.getBytes(),SerializeUtil.serialize(student));
			System.out.println("Redishuancun-----------------"+studentKey+ jedis.get(studentKey.getBytes()) + "-----------------");	
		}finally {
			jedisPool.returnResource(jedis);
		}
	}
	@Override
	public void deleteStudentById(int id) {
		studentMapper.deleteStudentById(id);
	}

	@Override
	public void updateStudentById(Student student) {
		studentMapper.updateStudentById(student);
	}

	@Override
	public int getTotalStudentCount() {
		
		return studentMapper.getTotalStudentCount();
	}

	@Override
	public Student selectStudentByPhone(long studentPhone) {
		jedis = jedisPool.getResource();
		Student student = null;
		String studentKey = MemcachedKeyUtil.getStudentsKey(studentPhone);
		try{
			byte[] value = jedis.get(studentKey.getBytes());
			student = (Student)SerializeUtil.unserialize(value);
			if(student == null) {
				student = studentMapper.selectStudentByPhone(studentPhone);
				if(student != null) {
					jedis.set(studentKey.getBytes(),SerializeUtil.serialize(student));
					System.out.println("Redishuancun-----------------"+studentKey+ jedis.get(studentKey.getBytes()) + "-----------------");
				}
			}
		}finally {
			jedisPool.returnResource(jedis);
		}
		return student;
	}

}
