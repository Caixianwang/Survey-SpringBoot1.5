/**
 * 
 */
package com.kukababy.dao.mongo.jpa;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kukababy.entity.mongo.SurveyMO;

/**
 * <b>描述</b>:
 * <blockquote>
 * <pre>
 * </pre>
 * </blockquote>
 * @author caixian_wang@sina.com
 * @date 2017年5月26日 下午12:29:32
 */
public interface SurveyRepository extends MongoRepository<SurveyMO, String> {


}
