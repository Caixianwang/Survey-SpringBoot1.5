package com.kukababy.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.kukababy.dao.mongo.MongoDAO;
import com.kukababy.dao.mongo.jpa.SurveyRepository;
import com.kukababy.entity.mongo.DatiBO;

@Service
public class CacheAnswerService {
	private static final Logger log = LoggerFactory.getLogger(CacheAnswerService.class);

	@Autowired(required = true)
	private MongoTemplate mongoTemplate;
	@Autowired(required = true)
	private MongoDAO mongoDAO;
	@Autowired(required = true)
	private SurveyRepository surveyRepository;
	
	public DatiBO getCacheAnswer(String anId,String tblName){
		return mongoTemplate.findById(anId, DatiBO.class, tblName);
	}

}
