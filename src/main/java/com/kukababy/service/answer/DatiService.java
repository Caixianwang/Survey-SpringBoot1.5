package com.kukababy.service.answer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kukababy.dao.mongo.jpa.SurveyRepository;
import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.entity.vo.DatiDTO;
import com.kukababy.entity.vo.WxCfg;
import com.kukababy.entity.vo.WxUser;
import com.kukababy.entity.vo.publish.QuotaDetail;
import com.kukababy.entity.vo.publish.QuotaGroup;
import com.kukababy.res.ResInfo;
import com.kukababy.service.comm.BaseService;
import com.kukababy.utils.Consts;
import com.kukababy.utils.RedisConsts;
import com.kukababy.utils.RequestUtil;

@Service
public class DatiService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(DatiService.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired(required = true)
	private SurveyRepository surveyRepository;

	/**
	 * 
	 * <b>描述:转换成数据库字段对应的值</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currVarAnMap
	 * @param var2FieldMap
	 * @return
	 */
	private Map<String, Object> getFieldAnMap(Map<String, String> var2FieldMap, Map<String, Object> currVarAnMap) {
		Map<String, Object> fieldAnMap = new HashMap();
		for (Map.Entry<String, Object> entry : currVarAnMap.entrySet()) {
			String varName = entry.getKey();
			Object anVal = entry.getValue();

			String fieldName = var2FieldMap.get(varName);
			if (fieldName != null) {
				fieldAnMap.put(fieldName, anVal);
			}
		}
		return fieldAnMap;
	}

	/**
	 * 
	 * <b>描述:配额验证</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param datiDTO
	 */
	public void validQuota(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		List<QuotaGroup> quotaGroups = datiDTO.getQuotaGroups();
		if (quotaGroups == null || quotaGroups.isEmpty()) {// 没有配额
			return;
		}
		// 当前提交的答案
		Map<String, Object> currFieldAnMap = datiDTO.getCurrFieldAnMap();

		boolean quotaChange = false;// 整体配额是否有变化，作为更新缓层的条件
		for (QuotaGroup quotaGroup : quotaGroups) {
			List<QuotaDetail> details = quotaGroup.getDetails();
			if (details.isEmpty()) {
				continue;
			}
			boolean quotaFull = true; // 一组配额是否满，只要任何一组配额满，标识配额满
			for (QuotaDetail quotaDetail : details) {
				String fieldName = quotaDetail.getFieldName();
				if (currFieldAnMap.get(fieldName) == null) {
					quotaFull = false;// 配额明细在答案里没有对应，可以确定配额未满
					continue;
				}
				String currFieldVal = currFieldAnMap.get(fieldName).toString();
				if (quotaDetail.getFieldVal().equals(currFieldVal)) {// 命中配额设置
					quotaChange = true;// 整体配额有变化
					quotaDetail.setRealSize(quotaDetail.getRealSize() + 1);// 配额加1
				}
				if (quotaDetail.getRealSize() <= quotaDetail.getMaxSize()) {
					quotaFull = false;// 有配额明细未满
				}
			}
			if (quotaFull) {// 有一组配额满了，终止答题，提示信息
				// log.info(JSON.toJSONString(currFieldAnMap));
				String quotaGroupName = quotaGroup.getName() == null ? "" : quotaGroup.getName();
				resInfo.setHasError(true).setStatus(ResInfo.FAIL);
				resInfo.setMsg("提示," + quotaGroupName + "：配额已满！");
				return;
			}
		}
		if (quotaChange) {// 配额有变化，需要更新缓层
			String surveyRedisKey = RedisConsts.surveyPublishPrefix + datiDTO.getRedisKey();
			// log.info(surveyRedisKey);
			// log.info(JSON.toJSONString(quotaGroups));
			this.stringRedisTemplate.boundHashOps(surveyRedisKey).put("quotaGroups", JSON.toJSONString(quotaGroups));
		}
	}

	/**
	 * 
	 * <b>描述:验证ip和cookie答题数量限制</b>: <blockquote>
	 * 
	 * <pre>
	 * 1、第一次答题时进行判断，已经答题了不进行验证
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param datiDTO
	 */
	public void validCookieCount(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		// 第一次答题时进行判断，已经答题了不进行验证,提供答题的速度
		if (StringUtils.isNotBlank(datiDTO.getAnId())) {
			return;
		}
		if (StringUtils.isNotBlank(datiDTO.getCookie())) {
			long cookieSize = datiDTO.getSurvey().getCookieSize();
			if (cookieSize > 0) {
				long realCookieSize = this.countCookieSize(datiDTO);
				if (realCookieSize >= cookieSize) {
					resInfo.setHasError(true).setStatus(ResInfo.FAIL).setMsg("抱歉，同一个机器已经多次答题！");
					return;
				}
			}
		}
	}

	public void validIpCount(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		// 第一次答题时进行判断，已经答题了不进行验证,提供答题的速度
		if (StringUtils.isNotBlank(datiDTO.getAnId())) {
			return;
		}
		if (StringUtils.isNotBlank(datiDTO.getIp())) {
			long ipSize = datiDTO.getSurvey().getIpSize();
			if (ipSize > 0) {
				long realIpSize = this.countIpSize(datiDTO);
				if (realIpSize >= ipSize) {
					resInfo.setHasError(true).setStatus(ResInfo.FAIL).setMsg("抱歉，同一个IP地址已经多次答题！");
					return;
				}
			}
		}
	}

	public void validWxCount(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		// 第一次答题时进行判断，已经答题了不进行验证,提供答题的速度
		if (StringUtils.isNotBlank(datiDTO.getAnId())) {
			return;
		}
		WxUser wxUser = datiDTO.getWxUser();
		if (wxUser != null) {// 有微信答题用户
			WxCfg wxCfg = datiDTO.getSurvey().getWxCfg();
			long wxSize = wxCfg.getWxSize();
			if (wxSize > 0) {
				long realWxSize = this.countWxAnSize(datiDTO);
				if (realWxSize >= wxSize) {
					resInfo.setHasError(true).setStatus(ResInfo.FAIL).setMsg("抱歉，同一 微信用户已经多次答题！");
					return;
				}
			}
		}

	}

	/**
	 * 
	 * <b>描述:验证问卷回收数</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param datiDTO
	 */
	public void validAnSize(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		// 第一次答题时进行判断，已经答题了不进行验证,提供答题的速度
		if (StringUtils.isNotBlank(datiDTO.getAnId())) {
			return;
		}

		long anSize = datiDTO.getSurvey().getAnSize();
		if (anSize > 0) {// 有问卷回收上限
			long realAnSize = this.countAnSize(datiDTO.getSurveyId());
			if (realAnSize >= anSize) {
				resInfo.setHasError(true).setStatus(ResInfo.FAIL).setMsg("抱歉，问卷回收数已满！");
				return;
			}
			realAnSize++;// 问卷数加一
			String surveyRedisKey = RedisConsts.surveyPublishPrefix + datiDTO.getRedisKey();
			this.stringRedisTemplate.boundHashOps(surveyRedisKey).put("realAnSize", "" + realAnSize);
		}
	}

	/**
	 * 
	 * <b>描述:核实答题回收数是否已满</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param datiDTO
	 */
	public void calcCheckAnSize(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		// 第一次答题时进行判断，已经答题了不进行验证,提供答题的速度
		if (StringUtils.isNotBlank(datiDTO.getAnId())) {
			return;
		}
		long anSize = datiDTO.getSurvey().getAnSize();
		if (anSize > 0) {// 有问卷回收上限
			long realAnSize = this.countAnSize(datiDTO.getSurveyId());
			if (realAnSize >= anSize) {
				resInfo.setHasError(true).setStatus(ResInfo.FAIL).setMsg("抱歉，问卷回收数已满！");
				return;
			}
		}
	}

	/**
	 * 
	 * <b>描述:从缓层里提取问卷的信息</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param datiDTO
	 */
	public void initDatiInfoFromCache(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		String redisKey = datiDTO.getSurveyId();

		datiDTO.setRedisKey(redisKey);
		// log.info(JSON.toJSONString(datiDTO));
		String surveyRedisKey = RedisConsts.surveyPublishPrefix + redisKey;
		BoundHashOperations<String, Object, Object> cacheMap = this.stringRedisTemplate.boundHashOps(surveyRedisKey);
		SurveyMO survey = JSON.parseObject((String) cacheMap.get("survey"), SurveyMO.class);
		datiDTO.setSurvey(survey);

		if (survey == null) {
			resInfo.setHasError(true).setStatus(ResInfo.FAIL);
			resInfo.setCode(Consts.resCode01).setMsg("抱歉，问卷还没发布！");
			return;
		}

		List<QuotaGroup> quotaGroups = JSON.parseArray((String) cacheMap.get("quotaGroups"), QuotaGroup.class);
		datiDTO.setQuotaGroups(quotaGroups);

		Map<String, String> var2FieldMap = (Map) JSON.parseObject((String) cacheMap.get("var2FieldMap"));
		datiDTO.setVar2FieldMap(var2FieldMap);

		long realAnSize = Long.parseLong((String) cacheMap.get("realAnSize"));
		datiDTO.setRealAnSize(realAnSize);

	}

	public void getHisAn(HttpServletRequest req, String surveyId, Map<String, Object> resMap) {
		Cookie[] cookies = req.getCookies();
		String openId = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Consts.wxPageAuthCookie + surveyId)) {
					openId = cookie.getValue();
				}
			}
		}
		if (openId != null) {
			Criteria cr = Criteria.where("wxOpenid").is(openId);
			Query query = new Query(cr);
			List<Map> ans = mongoTemplate.find(query, Map.class, "an_" + surveyId);
			if (ans.size() > 0) {
				resMap.put("hisAn", ans.get(0).get("hisAn"));
				resMap.put("anId", ans.get(0).get("_id"));
				log.info(JSON.toJSONString(ans.get(0).get("hisAn"), true));
			}
		}
	}

	/**
	 * 检查微信答题配置相关信息,通过一开始的url提交，首次验证，看是否需要进入微信授权页面 <br/>
	 * 1、如果必须微信答题，浏览器必须是微信环境<br/>
	 * 2、如果要求提供微信用户信息，并且是微信浏览器
	 */
	public void checkWxCfgDatiStart(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		boolean isWxBrowser = RequestUtil.isWxBrowser(datiDTO.getUserAgent());// 判断是否微信浏览器
		SurveyMO survey = datiDTO.getSurvey();
		// 必须微信答题，但不是微信浏览器
		if (survey.isRequiredWx() && !isWxBrowser) {
			resInfo.setHasError(true).setStatus(ResInfo.FAIL).setCode(Consts.resWxCode10);
			resInfo.setMsg("此问卷需要通过微信访问，请用微信扫一扫答题");
			return;

		}
		WxCfg wxCfg = survey.getWxCfg();
		if (isWxBrowser && wxCfg.isUseUser()) {// 微信环境，并且可能需要用户信息
			if (datiDTO.getWxUser() != null && datiDTO.getWxUser().getOpenid() != null) {// 已经有用户了
				String pageAuthKey = RedisConsts.pageOpenIdPrefix + datiDTO.getWxUser().getOpenid();
				long expire = stringRedisTemplate.getExpire(pageAuthKey);// 剩余多长时间
				if (expire > 0 && (RedisConsts.pageOpenIdTimeout - expire < 2 * 24 * 60 * 60)) {// 大于2天
					return;// 有微信用户数据了，刚刚授权
				}
			}
			String code = Consts.resWxCode11;// 微信必须登录，只有登录按钮
			if (!wxCfg.isLogin()) {
				code = Consts.resWxCode12;// 微信可以不登录,登录和不登录按钮都出现
			}
			resInfo.setHasError(true).setStatus(ResInfo.FAIL).setCode(code);
			resInfo.setMsg("抱歉，需要微信用户授权！");
			return;
		}
	}

	public void anDateCtl(ResInfo resInfo, DatiDTO datiDTO) {

		if (resInfo.isHasError()) {
			return;
		}
		SurveyMO survey = datiDTO.getSurvey();
		if (survey.isDateCtl()) {// 有时间范围控制
			Date currDate = new Date();
			if (survey.getStartDate() != null && survey.getStartDate().after(currDate)) {
				resInfo.setHasError(true).setStatus(ResInfo.FAIL);
				resInfo.setMsg("很抱歉，此问卷将于" + sdf.format(survey.getStartDate()) + "开放，请到时再重新打开此页面进行填写！");
				return;
			}
			if (survey.getEndDate() != null && survey.getEndDate().before(currDate)) {
				resInfo.setHasError(true).setStatus(ResInfo.FAIL);
				resInfo.setMsg("很抱歉，此问卷已经于" + sdf.format(survey.getEndDate()) + "结束，不能再接受新的答卷！");
				return;
			}
		}

	}

	/**
	 * 
	 * <b>描述:提交答案时进行检查，是否绕过微信进行提交</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param datiDTO
	 */
	public void validWxCfgDatiSubmit(ResInfo resInfo, HttpServletRequest req, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		boolean isWxBrowser = RequestUtil.isWxBrowser(datiDTO.getUserAgent());// 判断是否微信浏览器
		SurveyMO survey = datiDTO.getSurvey();
		// 必须微信答题，但不是微信浏览器
		if (survey.isRequiredWx() && !isWxBrowser) {
			resInfo.setHasError(true).setStatus(ResInfo.FAIL).setMsg("抱歉，需要在微信平台答题！");
			return;
		}
		WxCfg wxCfg = survey.getWxCfg();
		if (isWxBrowser && wxCfg.isUseUser()) {// 微信环境，并且可能需要用户信息
			if (wxCfg.isLogin()) {// 必须要登录的
				if (datiDTO.getWxUser() == null) {
					resInfo.setHasError(true).setStatus(ResInfo.FAIL).setMsg("抱歉，需要微信用户登录授权！");
					return;
				}
			}
		}
	}

	/**
	 * 
	 * <b>描述:提交答案时，进行版本号验证</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param datiDTO
	 */
	public void validAnVersion(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}

		boolean versionChange = false;

		if (datiDTO.getVersion() != datiDTO.getSurvey().getPublishTime().getTime()) {
			versionChange = true;
		}

		if (versionChange) {
			resInfo.setHasError(true).setStatus(ResInfo.FAIL).setMsg("抱歉，您的问卷版本已经更新，请刷新本页面重新填写问卷");
			return;
		}

	}

	/**
	 * 
	 * <b>描述:答题变量值转换为数据库字段对应的值</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param datiDTO
	 */
	public void initVarAn2Field(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return;
		}
		Map<String, Object> currFieldAnMap = getFieldAnMap(datiDTO.getVar2FieldMap(), datiDTO.getCurrVarAnMap());
		datiDTO.setCurrFieldAnMap(currFieldAnMap);
	}

	/**
	 * 
	 * <b>描述:从缓层提取答题需要的问卷和问题数据</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> datiSurveyDataByCache(ResInfo resInfo, DatiDTO datiDTO) {
		if (resInfo.isHasError()) {
			return null;
		}
		String redisKey = datiDTO.getSurveyId();

		datiDTO.setRedisKey(redisKey);
		Map<String, Object> resMap = new HashMap();
		String surveyRedisKey = RedisConsts.surveyPublishPrefix + redisKey;
		BoundHashOperations<String, Object, Object> cacheSurveyMap = this.stringRedisTemplate.boundHashOps(surveyRedisKey);

		if (cacheSurveyMap != null) {
			String strSurvey = (String) cacheSurveyMap.get("survey");
			if (strSurvey != null) {
				SurveyMO survey = JSON.parseObject(strSurvey, SurveyMO.class);

				String quessRedisKey = RedisConsts.quessPublishPrefix + redisKey;
				BoundHashOperations<String, Object, Object> cacheQuessMap = this.stringRedisTemplate.boundHashOps(quessRedisKey);
				List<Map<String, Object>> quess = (List) JSON.parseArray((String) cacheQuessMap.get("quess"));

				resMap.put("survey", survey);
				resMap.put("quess", quess);
				datiDTO.setSurvey(survey);

				resMap.put("version", survey.getPublishTime().getTime());

			} else {
				resInfo.setHasError(true).setStatus(ResInfo.FAIL);
				resInfo.setCode(Consts.resCode01).setMsg("抱歉，问卷还没发布！");
				return null;
			}
		} else {
			resInfo.setHasError(true).setStatus(ResInfo.FAIL);
			resInfo.setCode(Consts.resCode01).setMsg("抱歉，问卷还没发布！");
			return null;
		}
		return resMap;
	}

}
