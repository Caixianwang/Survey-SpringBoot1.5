package com.kukababy;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.kukababy.dao.mongo.MongoDAO;
import com.kukababy.dao.mongo.jpa.UserRepository;
import com.kukababy.service.data.DataService;
import com.kukababy.service.data.StatChartService;
import com.kukababy.service.design.SurveyDesignService;
import com.mongodb.BasicDBObject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTests {
	private static final Logger log = LoggerFactory.getLogger(MongoTests.class);
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MongoDAO mongoDAO;

	@Autowired
	private DataService dataService;
	@Autowired
	private StatChartService dataChartService;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SurveyDesignService surveyDesignService;
	
	
	@Test
	public void test04() {
		surveyDesignService.backSurveyFromRedis("1su2EGUyKZ");
		//dataChartService.quesChart(new Pager(), "1su2EuOhop", false);
	}
	
	@Test
	public void test03() {
		int seq = dataChartService.getSeq("user");
		log.info(""+seq);
		//dataChartService.quesChart(new Pager(), "1su2EuOhop", false);
	}
	
	@Test
	public void test02() {
		Map<String,Object> map = dataChartService.matrixCheckboxData("1qu2EuOhqg", new BasicDBObject());
		//Map<String,Object> map = dataChartService.matrixRadioChart("1qu2EuOhqe", new BasicDBObject(), "an_wcx");
		//Map<String,Object> map = dataChartService.checkboxChart("1qu2EuOhou", new BasicDBObject(), "an_wcx");
		log.info(JSON.toJSONString(map));
		//dataChartService.quesChart(new Pager(), "1su2EuOhop", false);
	}
	
	@Test
	public void test01() {
		
		List list = mongoTemplate.getCollection("an_wcx").distinct("P_1f2EuOhot");
		log.info(JSON.toJSONString(list));
	}

	@Test
	public void contextLoads() {
	}

	

}
