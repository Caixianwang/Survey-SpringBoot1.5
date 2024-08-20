package com.kukababy.web.design;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.kukababy.common.ThreadCache;
import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.entity.mongo.bo.PageBO;
import com.kukababy.entity.mongo.bo.QuotaGroupBO;
import com.kukababy.entity.mongo.bo.SurveyCfgBO;
import com.kukababy.entity.vo.UserVO;
import com.kukababy.entity.vo.WxCfg;
import com.kukababy.key.KeyUtils;
import com.kukababy.pager.Page;
import com.kukababy.pager.Pager;
import com.kukababy.res.ResInfo;
import com.kukababy.service.LoginService;
import com.kukababy.service.design.QuesDesignService;
import com.kukababy.service.design.SurveyDesignService;
import com.kukababy.web.base.BaseControl;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年4月25日 上午9:40:43
 */
@RestController
@RequestMapping("/design")
public class SurveyDesignControl extends BaseControl {

	private static final Logger log = LoggerFactory.getLogger(SurveyDesignControl.class);

	@Autowired(required = true)
	private SurveyDesignService surveyDesignService;
	@Autowired(required = true)
	private QuesDesignService quesDesignService;
	@Autowired
	private LoginService loginService;

	/**
	 * 
	 * <b>描述:从缓层里恢复问卷</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyId
	 * @return
	 */
	@RequestMapping(value = "/backSurvey")
	public ResInfo backSurvey(@RequestParam String surveyId) {
		ResInfo resInfo = new ResInfo();
		boolean success = surveyDesignService.backSurveyFromRedis(surveyId);
		if (!success) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("恢复错误");
		}
		return resInfo;
	}

	@RequestMapping(value = "/refSurveyLib")
	public ResInfo refSurveyLib(@RequestParam String surveyId, @RequestParam String title) {
		ResInfo resInfo = new ResInfo();
		surveyDesignService.refSurveyLib(surveyId, title);
		return resInfo;
	}

	@RequestMapping(value = "/copySurvey")
	public ResInfo copySurvey(@RequestParam String surveyId, @RequestParam String title) {
		ResInfo resInfo = new ResInfo();
		surveyDesignService.copySurvey(surveyId, title);
		return resInfo;
	}

	@RequestMapping(value = "/updateSurvey", method = RequestMethod.POST)
	public ResInfo updateSurvey(@RequestBody Map<String, Object> params) {
		ResInfo res = new ResInfo();
		log.info("params=" + JSON.toJSONString(params));
		surveyDesignService.updateSurvey(params);
		return res;
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
	 * @return
	 */
	@RequestMapping(value = "/stopSurvey")
	public ResInfo stopSurvey(@RequestParam String surveyId) {

		ResInfo resInfo = new ResInfo();
		surveyDesignService.stopSurvey(surveyId);
		return resInfo;
	}

	@RequestMapping(value = "/surveyPublish")
	public ResInfo surveyPublish(@RequestParam String surveyId) {

		ResInfo resInfo = new ResInfo();
		surveyDesignService.surveyPublish(surveyId);
		return resInfo;
	}

	@RequestMapping(value = "/insertPage")
	public ResInfo insertPage(@RequestParam String surveyId) {
		ResInfo res = new ResInfo();
		log.info("insertPage=" + surveyId);
		String pageId = surveyDesignService.insertPage(surveyId);
		Map map = new HashMap();
		map.put("pageId", pageId);
		res.setRes(map);
		return res;
	}

	@RequestMapping(value = "/delPage")
	public ResInfo delPage(@RequestParam String surveyId, @RequestParam String pageId) {
		ResInfo res = new ResInfo();
		surveyDesignService.delPage(surveyId, pageId);
		return res;
	}

	@RequestMapping(value = "/delQues")
	public ResInfo delQues(@RequestParam String surveyId, @RequestParam String pageId, @RequestParam String quesId) {
		ResInfo res = new ResInfo();
		surveyDesignService.delQues(surveyId, pageId, quesId);
		return res;
	}

	@RequestMapping(value = "/surveyList", method = RequestMethod.POST)
	public ResInfo surveyList(@RequestBody Pager<SurveyMO> pager) {
		log.info("surveyList=" + JSON.toJSONString(pager));
		ResInfo resInfo = new ResInfo();
		surveyDesignService.surveyList(pager);
		resInfo.setRes(new Page(pager.getTotal(), pager.getRows()));
		return resInfo;
	}

	@RequestMapping(value = "/surveyLibList", method = RequestMethod.POST)
	public ResInfo surveyLibList(@RequestBody Pager<SurveyMO> pager, HttpServletRequest request) {
		log.info("surveyList=" + JSON.toJSONString(pager));
		ResInfo resInfo = new ResInfo();
		String share = request.getParameter("share");
		surveyDesignService.surveyLibList(pager, share);
		resInfo.setRes(new Page(pager.getTotal(), pager.getRows()));
		return resInfo;
	}

	@RequestMapping(value = "/editSurvey", method = RequestMethod.POST)
	public ResInfo editSurvey(@RequestBody SurveyMO surveyMO) {
		ResInfo res = new ResInfo();
		log.info("editSurvey=" + JSON.toJSONString(surveyMO));
		String surveyId = surveyDesignService.editSurvey(surveyMO);
		Map map = new HashMap();
		map.put("surveyId", surveyId);
		res.setRes(map);
		return res;
	}

	@RequestMapping(value = "/updatePageBOs", method = RequestMethod.POST)
	public ResInfo updatePageBOs(HttpServletRequest req, @RequestBody List<PageBO> pageBOs) {
		ResInfo res = new ResInfo();
		log.info("updatePageBOs=" + JSON.toJSONString(pageBOs));
		log.info(req.getParameter("surveyId"));
		surveyDesignService.updatePageBOs(req.getParameter("surveyId"), pageBOs);
		return res;
	}

	@RequestMapping(value = "/updateQuotaGroups", method = RequestMethod.POST)
	public ResInfo updateQuotaGroups(HttpServletRequest req, @RequestBody List<QuotaGroupBO> quotas) {
		ResInfo res = new ResInfo();
		log.info("updateQuotaGroups=" + JSON.toJSONString(quotas));
		log.info(req.getParameter("surveyId"));
		surveyDesignService.updateQuotaGroups(req.getParameter("surveyId"), quotas);
		return res;
	}

	@RequestMapping(value = "/updateSurveyCfg", method = RequestMethod.POST)
	public ResInfo updateSurveyCfg(HttpServletRequest req, @RequestBody SurveyCfgBO surveyCfgBO) {
		ResInfo res = new ResInfo();
		log.info("updateSurveyCfg=" + JSON.toJSONString(surveyCfgBO));
		log.info(req.getParameter("surveyId"));
		surveyDesignService.updateSurveyCfg(req.getParameter("surveyId"), surveyCfgBO);
		return res;
	}

	@RequestMapping(value = "/updateSurveyWxCfg", method = RequestMethod.POST)
	public ResInfo updateSurveyWxCfg(HttpServletRequest req, @RequestBody WxCfg wxCfg) {
		ResInfo res = new ResInfo();
		log.info("updateSurveyWxCfg=" + JSON.toJSONString(wxCfg));
		log.info(req.getParameter("surveyId"));
		log.info(req.getParameter("requiredWx"));
		boolean requiredWx = Boolean.parseBoolean(req.getParameter("requiredWx"));
		surveyDesignService.updateSurveyWxCfg(req.getParameter("surveyId"), requiredWx, wxCfg);
		return res;
	}

	@RequestMapping(value = "/getPrimaryId")
	public ResInfo getPrimaryId(@RequestParam String tblName) {
		ResInfo res = new ResInfo();
		String primaryId = KeyUtils.getInstance().getUUID(tblName);
		Map map = new HashMap();
		map.put("primaryId", primaryId);
		res.setRes(map);
		return res;
	}

	@RequestMapping(value = "/getFieldId")
	public ResInfo getFieldId() {
		ResInfo res = new ResInfo();
		String fieldId = KeyUtils.getInstance().getShortUUID("fd");
		Map map = new HashMap();
		map.put("fieldId", fieldId);
		res.setRes(map);
		return res;
	}

	@RequestMapping(value = "/getSurvey")
	public ResInfo getSurvey(@RequestParam String surveyId) {
		ResInfo res = new ResInfo();
		SurveyMO surveyMO = surveyDesignService.getSurvey(surveyId);
		res.setRes(surveyMO);
		return res;
	}

	/**
	 * 
	 * <b>描述:编辑时，从数据库提取问卷相关数据</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getEditSurveyStructure")
	public ResInfo getAnSurveyStructure(@RequestParam String surveyId) {
		ResInfo res = new ResInfo();
		SurveyMO surveyMO = surveyDesignService.getSurvey(surveyId);
		List<Map<String, Object>> quess = surveyDesignService.queryQues(surveyId);

		Map<String, Object> resMap = new HashMap();
		resMap.put("survey", surveyMO);
		resMap.put("quess", quess);
		res.setRes(resMap);
		return res;
	}

	@RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
	public ResInfo getUserInfo() {
		ResInfo res = new ResInfo();
		Map<String, Object> userInfo = new HashMap();
		UserVO user = ThreadCache.getUser();
		userInfo.put("userName", user.getUserName());
		userInfo.put("userId", user.getUserId());
		userInfo.put("key", user.getKey());
		userInfo.put("hasPd", user.isHasPd());
		userInfo.put("wxNickname", user.getWxNickname());
		userInfo.put("wxUse", user.isWxUse());
		res.setRes(userInfo);
		return res;
	}

	@RequestMapping(value = "/updatePasswd", method = RequestMethod.POST)
	public ResInfo updatePasswd(@RequestBody Map<String, String> reqMap) {
		// log.info("inputScrollData=" + JSON.toJSONString(reqMap));
		ResInfo resInfo = new ResInfo();

		String oldPasswd = reqMap.get("old");
		String newPasswd = reqMap.get("new");
		String newOk = reqMap.get("newOk");
		UserVO userVO = ThreadCache.getUser();
		
		if (StringUtils.isBlank(newPasswd) || StringUtils.isBlank(newOk)) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("密码都必须输入！");
			return resInfo;
		}
		
		if (userVO.isHasPd()) {
			if (StringUtils.isBlank(oldPasswd)) {
				resInfo.setStatus(ResInfo.FAIL);
				resInfo.setMsg("旧密码必须输入！");
				return resInfo;
			}
			boolean success = this.surveyDesignService.validPasswdByUserId(userVO.getUserId(), oldPasswd);
			if (!success) {
				resInfo.setStatus(ResInfo.FAIL);
				resInfo.setMsg("旧密码不正确！");
				return resInfo;
			}
		}
		if (!newPasswd.equals(newOk)) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("新密码和确认密码不相等！");
			return resInfo;
		}
		this.surveyDesignService.updatePasswd(userVO.getUserId(), newPasswd);
		if(!userVO.isHasPd()){
			userVO.setHasPd(true);
			loginService.setUser2Redis(userVO);
		}
		return resInfo;
	}

}
