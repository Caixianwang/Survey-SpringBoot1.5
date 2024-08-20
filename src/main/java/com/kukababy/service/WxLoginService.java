package com.kukababy.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.kukababy.dao.mongo.jpa.UserRepository;
import com.kukababy.entity.mongo.UserMO;
import com.kukababy.key.KeyUtils;
import com.kukababy.service.comm.BaseService;

@Service
public class WxLoginService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(WxLoginService.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private UserRepository userRepository;

	public void cancelBindWx(String userId) {
		this.mongoDAO.updateKeyVal(userId, "wxUse", false, "m_user");
	}

	/**
	 * 
	 * <b>描述:通过微信openid，得到用户信息</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param openid
	 * @return
	 */
	public UserMO getUserByOpenid(String openid) {
		Criteria cr = Criteria.where("openid").is(openid);
		Query query = new Query(cr);
		UserMO user = this.mongoTemplate.findOne(query, UserMO.class);
		return user;
	}

	public UserMO getUserByOpenidAndUse(String openid) {
		Criteria cr = Criteria.where("openid").is(openid).and("wxUse").is(true);
		Query query = new Query(cr);
		UserMO user = this.mongoTemplate.findOne(query, UserMO.class);
		return user;
	}

	/**
	 * 
	 * <b>描述:对已存在的未绑定微信用户，启用</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param userId
	 */
	public void updateWxUserAndNickName(String userId, String nickname) {
		// Query query = new Query();
		// query.addCriteria(Criteria.where("openid").is(openid));
		// Update update = Update.update("wxUse", true);
		// mongoTemplate.upsert(query, update, UserMO.class);
		Map<String, Object> map = new HashMap();
		map.put("wxUse", true);
		map.put("nickname", nickname);
		this.mongoDAO.updateMulKeyVal(userId, map, "m_user");

	}

	/**
	 * 
	 * <b>描述:通过微信openid创建用户</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param openid
	 */
	public UserMO createUserByOpenid(String openid, String nickname) {

		UserMO user = new UserMO();
		user.setUserId(KeyUtils.getInstance().getShortUUID("u"));
		user.setUserName(KeyUtils.getInstance().getShortUUID("u"));
		user.setOpenid(openid);
		user.setNickname(nickname);
		user.setWxUse(true);
		this.userRepository.save(user);
		return user;
	}

	public void bindUserByOpenid(String userId, String openid, String nickname) {

		Map<String, Object> map = new HashMap();
		map.put("wxUse", true);
		map.put("nickname", nickname);
		map.put("openid", openid);
		this.mongoDAO.updateMulKeyVal(userId, map, "m_user");
	}

	public void otherWxUserDel(String userId, String openid) {

		Query query = new Query();
		query.addCriteria(Criteria.where("openid").is(openid).and("userId").ne(userId));
		Update update = new Update();
		update.unset("openid");
		update.unset("wxUse");
		update.unset("nickname");
		mongoTemplate.updateMulti(query, update, UserMO.class);

	}

}
