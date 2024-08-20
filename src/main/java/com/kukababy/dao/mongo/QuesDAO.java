package com.kukababy.dao.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kukababy.pager.Pager;

@Repository
public class QuesDAO {
	private static final Logger log = LoggerFactory.getLogger(QuesDAO.class);

	@Autowired(required = true)
	private MongoDAO mongoDAO;

	/**
	 * 
	 * <b>描述</b>: <blockquote>
	 * 
	 * <pre>
	 * 按title模糊查询问题
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param pager
	 */
	public void queryQues_TitleLike(Pager pager) {
		
	}

}
