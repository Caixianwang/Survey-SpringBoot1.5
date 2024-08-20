package com.kukababy.service.design;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kukababy.common.ThreadCache;
import com.kukababy.entity.mongo.OtherMO;
import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.entity.mongo.UserMO;
import com.kukababy.entity.mongo.bo.PageBO;
import com.kukababy.entity.mongo.bo.QuotaGroupBO;
import com.kukababy.entity.mongo.bo.SurveyCfgBO;
import com.kukababy.entity.mongo.ques.EndItemBO;
import com.kukababy.entity.mongo.ques.EndQuesBO;
import com.kukababy.entity.mongo.ques.StartQuesBO;
import com.kukababy.entity.vo.UserVO;
import com.kukababy.entity.vo.WxCfg;
import com.kukababy.entity.vo.publish.QuotaGroup;
import com.kukababy.key.KeyUtils;
import com.kukababy.pager.FilterEL;
import com.kukababy.pager.Pager;
import com.kukababy.service.comm.BaseService;
import com.kukababy.utils.Consts;
import com.kukababy.utils.MD5;
import com.kukababy.utils.RedisConsts;

@Service
public class SurveyDesignService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(SurveyDesignService.class);

	public boolean backSurveyFromRedis(String surveyId) {
		boolean success = false ;
		String redisKey = surveyId;
		String surveyRedisKey = RedisConsts.surveyPublishPrefix + redisKey;
		BoundHashOperations<String, Object, Object> cacheSurveyMap = this.stringRedisTemplate.boundHashOps(surveyRedisKey);
	
		if (cacheSurveyMap != null) {
			String strSurvey = (String) cacheSurveyMap.get("survey");
			if (strSurvey != null) {
				SurveyMO survey = JSON.parseObject(strSurvey, SurveyMO.class);
				String quessRedisKey = RedisConsts.quessPublishPrefix + redisKey;
				BoundHashOperations<String, Object, Object> cacheQuessMap = this.stringRedisTemplate.boundHashOps(quessRedisKey);
				String strQuess = (String) cacheQuessMap.get("quess");
				if (strQuess != null) {
					List<Map> oldQuess = this.getFieldsQuess(surveyId,"quesId");
					List<Map<String, Object>> quess = (List) JSON.parseArray(strQuess);
					for(Map<String, Object> ques:quess){
						ques.put("_id", KeyUtils.getInstance().getShortUUID("qk"));//新的主鍵
					}
					this.mongoTemplate.insert(quess, "m_ques");
					this.surveyRepository.save(survey);
					
					Set<String> delQuesKeys = new HashSet();
					for(Map oldQues:oldQuess){
						delQuesKeys.add((String)oldQues.get("_id"));
					}
					this.delQuess(surveyId,delQuesKeys);//移除废弃的问题
					success = true ;
				}
			}
		}
		return success;
	}

	/**
	 * 
	 * <b>描述:通过问卷模板创建问卷</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param title
	 */
	public void refSurveyLib(String surveyId, String title) {
		SurveyMO survey = this.getSurvey(surveyId);
		List<Map> quess = this.getQuess(surveyId);

		if (StringUtils.isBlank(title)) {
			survey.setTitle(survey.getTitle() + "_引用");
		} else {
			survey.setTitle(title);
		}
		survey.setTemplate(Consts.NO);
		survey.setUserId(ThreadCache.getUser().getUserId());

		this.copySurveyAndQuesCommon(survey, quess);
	}

	/**
	 * 
	 * <b>描述:当前问卷复制一份</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param title
	 */
	public void copySurvey(String surveyId, String title) {
		SurveyMO survey = this.getSurvey(surveyId);
		List<Map> quess = this.getQuess(surveyId);
		if (StringUtils.isBlank(title)) {
			survey.setTitle(survey.getTitle() + "_复制");
		} else {
			survey.setTitle(title);
		}
		this.copySurveyAndQuesCommon(survey, quess);
	}

	/**
	 * 
	 * <b>描述:复制问卷产生的新的公共属性，问题的主键新生成</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param newSurveyId
	 * @param quess
	 */
	private void copySurveyAndQuesCommon(SurveyMO survey, List<Map> quess) {

		String newSurveyId = KeyUtils.getInstance().getShortUUID("su");
		survey.setSurveyId(newSurveyId);// 新的问卷ID
		survey.setCreated(new Date());
		survey.setPub(Consts.NO);
		survey.setPublishState(Consts.NO);
		survey.setPublishTime(null);

		for (Map ques : quess) {
			String newQuesKey = KeyUtils.getInstance().getShortUUID("qk");
			ques.put("_id", newQuesKey);// 替换新的问题key
			ques.put("surveyId", newSurveyId);// 替换新的问卷key
		}
		this.mongoTemplate.insert(quess, "m_ques");
		this.surveyRepository.save(survey);
	}

	/**
	 * 
	 * <b>描述:更新问卷信息</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param params
	 */
	public void updateSurvey(Map<String, Object> params) {
		String surveyId = (String) params.get("surveyId");
		SurveyMO surveyMO = this.getSurvey(surveyId);
		this.copyProperties(params, surveyMO);
		surveyRepository.save(surveyMO);
	}

	/**
	 * 
	 * <b>描述:停止问卷发布</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param test
	 */
	public void stopSurvey(String surveyId) {
		String redisKey = surveyId;

		String surveyRedisKey = RedisConsts.surveyPublishPrefix + redisKey;
		this.stringRedisTemplate.delete(surveyRedisKey);// 删除缓层
		String quessRedisKey = RedisConsts.quessPublishPrefix + redisKey;
		this.stringRedisTemplate.delete(quessRedisKey);// 删除缓层
		// 更新问卷的停止状态和停止日期
		this.stopSurveyState(surveyId);
	}

	/**
	 * 
	 * <b>描述:发布问卷</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param test
	 */
	public void surveyPublish(String surveyId) {

		SurveyMO survey = this.getSurvey(surveyId);
		List<Map<String, Object>> quess = this.queryQues(surveyId);
		long realAnSize = this.countAnSize(surveyId);
		// 变量名对应字段名
		Map<String, String> var2FieldMap = this.var2FieldMap(quess);
		// 配额情况
		List<QuotaGroup> quotaGroups = this.cacheQuotaGroup(surveyId, var2FieldMap, survey.getQuotaGroups());

		String redisKey = surveyId;

		String surveyRedisKey = RedisConsts.surveyPublishPrefix + redisKey;
		String quessRedisKey = RedisConsts.quessPublishPrefix + redisKey;

		this.stringRedisTemplate.delete(surveyRedisKey);// 删除问卷缓层
		this.stringRedisTemplate.delete(quessRedisKey);// 删除问题缓层
														// 问卷单独放，因为数据比较大，答题时只是初始化问卷时使用

		Date publishTime = new Date();

		survey.setPublishTime(publishTime);
		survey.setPublishState(Consts.YES);

		Map<String, Object> surveyMap = new HashMap();
		surveyMap.put("survey", JSON.toJSONString(survey));
		surveyMap.put("quotaGroups", JSON.toJSONString(quotaGroups));
		surveyMap.put("realAnSize", "" + realAnSize);
		surveyMap.put("var2FieldMap", JSON.toJSONString(var2FieldMap));
		this.stringRedisTemplate.boundHashOps(surveyRedisKey).putAll(surveyMap);// 问卷主要信息，重新加入缓层

		Map<String, Object> quessMap = new HashMap();
		quessMap.put("quess", JSON.toJSONString(quess));
		this.stringRedisTemplate.boundHashOps(quessRedisKey).putAll(quessMap);// 问题主要信息，重新加入缓层

		// 更新问卷的发布状态和发布日期
		this.updatePublishState(surveyId, publishTime);
	}

	public String insertPage(String surveyId) {
		SurveyMO surveyMO = this.getSurvey(surveyId);
		String pageId = KeyUtils.getInstance().getShortUUID("pa");
		PageBO pageBO = new PageBO();
		pageBO.setPageId(pageId);
		List<PageBO> pageBOs = surveyMO.getPageBOs();
		pageBOs.add(pageBO);
		updatePageBOs(surveyId, pageBOs);
		return pageId;
	}

	/**
	 * 
	 * <b>描述:删除一个问题</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param pageId
	 * @param quesId
	 */
	public void delQues(String surveyId, String pageId, String quesId) {
		SurveyMO surveyMO = surveyRepository.findOne(surveyId);
		List<PageBO> pageBOs = surveyMO.getPageBOs();
		for (PageBO pageBO : pageBOs) {
			if (pageBO.getPageId().equals(pageId)) {
				for (String tmpQuesId : pageBO.getQuesIds()) {
					if (tmpQuesId.equals(quesId)) {
						pageBO.getQuesIds().remove(tmpQuesId);
						break;
					}
				}
				break;
			}
		}
		updatePageBOs(surveyId, pageBOs);
		Query query = new Query(Criteria.where("quesId").is(quesId).and("surveyId").is(surveyId));
		mongoTemplate.remove(query, "m_ques");

	}

	/**
	 * 
	 * <b>描述:删除一页</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param pageId
	 */
	public void delPage(String surveyId, String pageId) {
		SurveyMO surveyMO = surveyRepository.findOne(surveyId);
		List<PageBO> pageBOs = surveyMO.getPageBOs();
		List<String> quesIds = new ArrayList();
		for (PageBO pageBO : pageBOs) {
			if (pageBO.getPageId().equals(pageId)) {
				quesIds = pageBO.getQuesIds();
				pageBOs.remove(pageBO);
				break;
			}
		}
		updatePageBOs(surveyId, pageBOs);
		for (String quesId : quesIds) {
			Query query = new Query(Criteria.where("quesId").is(quesId).and("surveyId").is(surveyId));
			mongoTemplate.remove(query, "m_ques");
		}
	}

	public void updatePageBOs(String surveyId, List<PageBO> pageBOs) {
		mongoDAO.updateKeyVal(surveyId, "pageBOs", pageBOs, SurveyMO.TABLE_NAME);
	}

	/**
	 * 
	 * <b>描述:更新问卷相关样式之类配置</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param surveyCfgBO
	 */
	public void updateSurveyCfg(String surveyId, SurveyCfgBO surveyCfgBO) {
		mongoDAO.updateKeyVal(surveyId, "cfg", surveyCfgBO, SurveyMO.TABLE_NAME);
	}

	/**
	 * 
	 * <b>描述:更新微信配置</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param requiredWx
	 * @param wxCfg
	 */
	public void updateSurveyWxCfg(String surveyId, boolean requiredWx, WxCfg wxCfg) {
		Map<String, Object> map = new HashMap();
		map.put("requiredWx", requiredWx);
		map.put("wxCfg", wxCfg);
		mongoDAO.updateMulKeyVal(surveyId, map, SurveyMO.TABLE_NAME);
	}

	/**
	 * 
	 * <b>描述:更新配额</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @param QuotaBOs
	 */
	public void updateQuotaGroups(String surveyId, List<QuotaGroupBO> QuotaGroupBOs) {
		mongoDAO.updateKeyVal(surveyId, "quotaGroups", QuotaGroupBOs, SurveyMO.TABLE_NAME);
	}

	public void surveyList(Pager<SurveyMO> pager) {
		UserVO userVO = ThreadCache.getUser();
		pager.getFilters().add(new FilterEL("userId", userVO.getUserId()));
		pager.getFilters().add(new FilterEL("template", Consts.NO));
		mongoDAO.queryPager(pager, SurveyMO.class);
	}

	public void surveyLibList(Pager<SurveyMO> pager, String share) {
		UserVO userVO = ThreadCache.getUser();
		pager.getFilters().add(new FilterEL("template", Consts.YES));
		if (share.equals("0")) {
			pager.getFilters().add(new FilterEL("share", "Y"));
		} else {
			pager.getFilters().add(new FilterEL("userId", userVO.getUserId()));
		} 
//		else {
//			pager.setSysLogic(false);
//			pager.getSysFilters().add(new FilterEL("share", "Y"));
//			pager.getSysFilters().add(new FilterEL("userId", userVO.getUserId()));
//		}

		mongoDAO.queryPager(pager, SurveyMO.class);
	}

	public void pubSurveyList(Pager<SurveyMO> pager) {
		UserVO userVO = ThreadCache.getUser();
		pager.getFilters().add(new FilterEL("pub", Consts.YES));
		pager.getFilters().add(new FilterEL("publishState", Consts.YES));

		mongoDAO.queryPager(pager, SurveyMO.class);
	}

	public String editSurvey(SurveyMO surveyMO) {
		if (StringUtils.isBlank(surveyMO.getSurveyId())) {
			surveyMO.setSurveyId(KeyUtils.getInstance().getShortUUID("su"));
			UserVO userVO = ThreadCache.getUser();
			surveyMO.setUserId(userVO.getUserId());
			// 是问卷模板
			if (surveyMO.getTemplate().equals(Consts.YES) && userVO.getUserId().equals("001")) {
				surveyMO.setShare(Consts.YES);// 管理员做的问卷模板，默认分享到平台
			}
			this.initStartEndQues(surveyMO.getSurveyId());
		}
		if (surveyMO.getPageBOs().isEmpty()) {// 默认有一空页
			PageBO pageBO = new PageBO();
			pageBO.setPageId(KeyUtils.getInstance().getShortUUID("pa"));
			surveyMO.getPageBOs().add(pageBO);
		}
		surveyRepository.save(surveyMO);
		return surveyMO.getSurveyId();
	}

	private void initStartEndQues(String surveyId) {
		EndQuesBO endQuesBO = new EndQuesBO();// 创建默认结束题

		endQuesBO.setSurveyId(surveyId);
		endQuesBO.set_id(KeyUtils.getInstance().getShortUUID("qk"));
		endQuesBO.setQuesId(KeyUtils.getInstance().getShortUUID("qu"));
		OtherMO otherMO = this.otherRepository.findOne("end");
		EndItemBO endItemBO = new EndItemBO();
		endItemBO.setHtmlLabel(otherMO.getContent());
		endQuesBO.getRows().add(endItemBO);

		StartQuesBO startQuesBO = new StartQuesBO();// 创建默认开始题
		startQuesBO.set_id(KeyUtils.getInstance().getShortUUID("qk"));
		startQuesBO.setQuesId(KeyUtils.getInstance().getShortUUID("qu"));
		startQuesBO.setSurveyId(surveyId);
		otherMO = this.otherRepository.findOne("start");
		startQuesBO.setHtmlTitle(otherMO.getContent());
		List list = new ArrayList();
		list.add(startQuesBO);
		list.add(endQuesBO);
		mongoTemplate.insert(list, "m_ques");

	}

	public boolean validPasswdByUserId(String userId, String passwd) {
		UserMO user = this.userRepository.findOne(userId);
		if (user == null) {
			return false;
		} else {
			String md5pd = MD5.convert(passwd);
			if (md5pd.equals(user.getPasswd())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void updatePasswd(String userId, String passwd) {
		this.mongoDAO.updateFirst(userId, UserMO.class, "passwd", MD5.convert(passwd));
	}

}
