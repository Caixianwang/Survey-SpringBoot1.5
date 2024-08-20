package com.kukababy.dao.redis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;

@Repository
public class RedisDAO {
	private static final Logger log = LoggerFactory.getLogger(RedisDAO.class);
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/***
	 * 添加数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public void set(String key, Object obj) {
		this.set(key, obj, 0, null);
	}

	/***
	 * 添加数据 并且设置超时时间
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 * @return
	 */
	public void set(String key, Object obj, long timeout) {
		this.set(key, obj, timeout, TimeUnit.SECONDS);
	}

	/***
	 * 添加数据 并且设置超时时间和时间单位
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 * @return
	 */
	public void set(String key, Object obj, long timeout, TimeUnit timeUnit) {
		String value = null;
		if (obj instanceof String) {
			value = (String) obj;
		} else {
			value = JSON.toJSONString(obj);
		}
		if (timeout == 0) {
			this.stringRedisTemplate.opsForValue().set(key, value);
		} else {
			this.stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
		}
	}

	public String get(String key) {
		return this.stringRedisTemplate.opsForValue().get(key);
	}

	public <T> T get(String key, Class<T> cls) {
		String value = this.stringRedisTemplate.opsForValue().get(key);
		if(value==null){
			return null ;
		}
		return JSON.parseObject(value, cls);
	}

	/**
	 * 
	 * <b>描述</b>: <blockquote>
	 * 
	 * <pre>
	 * Zset结构，设置值
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param key
	 * @param val
	 * @param inc
	 */
	public void setZset(String key, String val, int inc) {
		stringRedisTemplate.boundZSetOps(key).incrementScore(val, inc);
	}

	/**
	 * 
	 * <b>描述</b>: <blockquote>
	 * 
	 * <pre>
	 * Zset 取部分数据
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> getZset(String key, int top) {
		return stringRedisTemplate.boundZSetOps(key).reverseRange(0, top);
	}

	public boolean has(String key) {
		return this.stringRedisTemplate.hasKey(key);
	}

	public void updateTimeout(String key, long timeout) {
		this.stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}

	public void updateTimeout(String key, long timeout, TimeUnit timeUnit) {
		this.stringRedisTemplate.expire(key, timeout, timeUnit);
	}

	public void getKeys(String prefix) {
		this.stringRedisTemplate.keys(prefix + "*");
	}
}
