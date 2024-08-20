package com.kukababy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.kukababy.dao.redis.RedisDAO;
import com.kukababy.utils.RedisConsts;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTests {
	private static final Logger log = LoggerFactory.getLogger(RedisTests.class);
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	//@Qualifier("RedisTemplate")
	private RedisTemplate redisTemplate;

	@Autowired
	private RedisDAO redisDAO;
	
	@Test
	public void test01() throws Exception {
		
		String pageAuthKey =RedisConsts.pageOpenIdPrefix +"opZHdsqXgeHYvfmYLCladIZ64EMg1" ;
		long s1 =  0 ;
		s1 = stringRedisTemplate.getExpire(pageAuthKey);
		log.info("s="+s1);
		Thread.sleep(5000);
		s1 = stringRedisTemplate.getExpire(pageAuthKey);
		log.info("s="+s1);
		
		//this.stringRedisTemplate.
		//redisDAO.set(RedisConsts.pageOpenIdPrefix + wxUser.getOpenid(), wxUser, RedisConsts.pageOpenIdTimeout);

	}

	
	
}
