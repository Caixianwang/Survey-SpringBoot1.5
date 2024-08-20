package com.kukababy.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kukababy.dao.mongo.jpa.UserRepository;
import com.kukababy.entity.mongo.UserMO;
import com.kukababy.entity.vo.UserVO;
import com.kukababy.key.KeyUtils;
import com.kukababy.service.comm.BaseService;
import com.kukababy.utils.MD5;
import com.kukababy.utils.RedisConsts;

@Service
public class LoginService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(LoginService.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private UserRepository userRepository;

	public void registerUserName(UserVO userVO) {
		String md5pd = MD5.convert(userVO.getPasswd());
		userVO.setUserId(KeyUtils.getInstance().getShortUUID("u"));
		UserMO user = new UserMO();
		user.setUserName(userVO.getUserName());
		user.setPasswd(md5pd);
		user.setUserId(userVO.getUserId());
		this.userMO2UserVO(user, userVO);
		userRepository.save(user);

	}

	/**
	 * 
	 * <b>描述</b>: <blockquote>
	 * 
	 * <pre>
	 * 1、用户名、密码验证
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param user
	 * @return
	 */
	public UserMO userLogin(UserVO userVO) {
		String md5pd = MD5.convert(userVO.getPasswd());
		Criteria cr = Criteria.where("passwd").is(md5pd).orOperator(Criteria.where("userName").is(userVO.getUserName()),
				Criteria.where("mobile").is(userVO.getUserName()));

		Query query = new Query(cr);
		UserMO user = this.mongoTemplate.findOne(query, UserMO.class);
		return user ;
	}
	
	public UserMO getUser(String userId){
		return userRepository.findOne(userId);
	}

	public boolean existsByUserName(String userName) {
		return userRepository.existsByUserName(userName);
	}
	public void modifyUserName(String userId,String userName){
		this.mongoDAO.updateFirst(userId, UserMO.class, "userName", userName);
	}

	public void setCaptcha(String cookie, String captcha) {
		redisDAO.set(RedisConsts.captchaPrefix + cookie, captcha, RedisConsts.captchaTimeout);
	}

	public String getCaptcha(String cookie) {
		return redisDAO.get(RedisConsts.captchaPrefix + cookie);
	}
	
	public void setWxScanUser2Redis(String primaryId,UserVO user) {
		redisDAO.set(RedisConsts.wxScanPrimaryPrefix + primaryId, user, 2 * 60);
	}
	
	public UserVO getWxScanUserOfRedis(String primaryId) {
		return redisDAO.get(RedisConsts.wxScanPrimaryPrefix + primaryId,UserVO.class);
	}
	
	public void delWxScanUserOfRedis(String primaryId) {
		stringRedisTemplate.delete(RedisConsts.wxScanPrimaryPrefix + primaryId);
	}
	
	public void userMO2UserVO(UserMO userMO,UserVO userVO) {
		userVO.setUserId(userMO.getUserId());
		userVO.setUserName(userMO.getUserName());//统一换成登录名
		userVO.setWxNickname(userMO.getNickname());
		userVO.setWxUse(userMO.isWxUse());
		if(userMO.getPasswd()==null){
			userVO.setHasPd(false);
		}
		userVO.setPasswd(null);
		userVO.setCaptcha(null);
	}

	public void setUser2Redis(UserVO userVO) {
		
		redisDAO.set(RedisConsts.cookieUserPrefix + userVO.getKey(), userVO, RedisConsts.cookieUserTimeout);
	}

	public UserVO getUserOfRedis(String cookie) {
		return redisDAO.get(RedisConsts.cookieUserPrefix + cookie, UserVO.class);
	}

	public void delUserCookieOfRedis(String cookie) {
		stringRedisTemplate.delete(RedisConsts.cookieUserPrefix + cookie);
	}

	public boolean hasUserCookieOfRedis(String cookie) {
		return redisDAO.has(RedisConsts.cookieUserPrefix + cookie);
	}

	public void updateUserTimeoutOfRedis(String cookie) {
		redisDAO.updateTimeout(RedisConsts.cookieUserPrefix + cookie, RedisConsts.cookieUserTimeout);
	}

}
