/**
 * 
 */
package com.kukababy.dao.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.kukababy.dao.BaseMongoDAO;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年7月8日 下午3:43:09
 */
@Repository
public class MongoDAO extends BaseMongoDAO{

	private static final Logger log = LoggerFactory.getLogger(MongoDAO.class);

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate){
		this.mongoTemplate = mongoTemplate ;
	}
}
