package com.kukababy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.mongodb.AggregationOptions;
import com.mongodb.AggregationOptions.OutputMode;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBObject;

@RunWith(SpringRunner.class)
@DataMongoTest
@EnableAutoConfiguration
public class MongoDaoTests {
	private static final Logger log = LoggerFactory.getLogger(MongoDaoTests.class);
	@Autowired
	private MongoTemplate mongoTemplate;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Test
	public void test05() {
		Map<String,Object> map = new HashMap();
		List list = new ArrayList();
		map.put("_id", "1");
		map.put("a", "qqq");
		list.add(map);
		map = new HashMap();
		map.put("_id", "2");
		map.put("b", "qqq");
		list.add(map);
		
		this.mongoTemplate.insert(list, "m_qq");
		
	}
	
	@Test
	public void test04() {
		Query query = new Query(Criteria.where("_id").is("user"));
		Update update = new Update().inc("seq", 1);
		Map<String,Object> map = mongoTemplate.findAndModify(query, update, Map.class,"m_seq");
		log.info(JSON.toJSONString(map));
				
	}
	@Test
	public void test03() {
		DBObject dbObject = mongoTemplate.findById("1qu2EuOhop", DBObject.class, "m_ques");
		log.info(JSON.toJSONString(dbObject.toMap()));
		
	}
	@Test
	public void test02() {
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject matchDetail = new BasicDBObject();
		BasicDBObject match = new BasicDBObject("$match", matchDetail);

		BasicDBObject groupDetail = new BasicDBObject("_id", null);
		groupDetail.put("count", new BasicDBObject("$sum", "$A_1f2Ev9xfZ"));
		groupDetail.put("count1", new BasicDBObject("$sum", "$A_1fvL8s3fZ"));
		groupDetail.put("count2", new BasicDBObject("$sum", "$E_1f2EuOhqgE_1fKisK4qg"));
		BasicDBObject group = new BasicDBObject("$group", groupDetail);

		pipe.add(match);
		pipe.add(group);
		log.info(pipe.toString());
		Cursor cursor = mongoTemplate.getCollection("an_wcx").aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		while (cursor.hasNext()) {
			BasicDBObject dbObject = (BasicDBObject) cursor.next();
			log.info(dbObject.toString());
		}	
	}
	
	@Test
	public void test01() {
		List<DBObject> pipe = new ArrayList<DBObject>();
		BasicDBObject matchDetail = new BasicDBObject();
		BasicDBObject match = new BasicDBObject("$match", matchDetail);

		BasicDBObject groupDetail = new BasicDBObject("_id", "$P_1f2EuOhot");
		groupDetail.put("count", new BasicDBObject("$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupDetail);

		pipe.add(match);
		pipe.add(group);
		log.info(pipe.toString());
		Cursor cursor = mongoTemplate.getCollection("an_wcx").aggregate(pipe,
				AggregationOptions.builder().allowDiskUse(true).outputMode(OutputMode.CURSOR).build());
		while (cursor.hasNext()) {
			BasicDBObject dbObject = (BasicDBObject) cursor.next();
			log.info(dbObject.toString());
		}	
	}

}
