package com.kukababy.web.answer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.entity.vo.DatiDTO;
import com.kukababy.entity.vo.WxCfg;
import com.kukababy.res.ResInfo;
import com.kukababy.service.answer.DatiService;
import com.kukababy.service.answer.DatiStoreService;
import com.kukababy.web.base.BaseAnControl;

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
@RequestMapping("/an")
public class DatiControl extends BaseAnControl {

	private static final Logger log = LoggerFactory.getLogger(DatiControl.class);

	@Autowired(required = true)
	private DatiService datiService;
	@Autowired(required = true)
	private DatiStoreService datiStoreService;

	@RequestMapping(value = "/dati", method = RequestMethod.POST)
	public ResInfo dati(HttpServletRequest req, @RequestBody Map<String, Object> params) {
		ResInfo resInfo = new ResInfo();

		//log.info("dati=" + JSON.toJSONString(params));
		DatiDTO datiDTO = new DatiDTO();
		// 请求参数转成datiDTO对应的字段信息
		this.readyAnRequestParams(datiDTO, params);
		// 验证答题版本号
		// DatiRequestUtils.validDatiVersion(datiDTO);
		// 初始化浏览器代理信息
		this.initUserAgent(req, datiDTO);

		// 从发布问卷的缓层里，获取相关信息，以及使用的表名和问卷redisKey名
		datiService.initDatiInfoFromCache(resInfo, datiDTO);
		// 还没产生答案时需要的信息
		if (StringUtils.isBlank(datiDTO.getAnId())) {
			// 初始化ip地址和通过ip得到区域信息
			this.initIpAndCity(resInfo, req, datiDTO);
			// 检查浏览器是否有答题cookie，并初始化datiDTO的cookie
			this.validSurveyCookieFromBrowser(resInfo, req, datiDTO);
			// 验证答题回收数
			datiService.calcCheckAnSize(resInfo, datiDTO);
			// 验证cookie和ip的限制答题数量是否超限制
			datiService.validIpCount(resInfo, datiDTO);
			datiService.validCookieCount(resInfo, datiDTO);

			// 初始化答题渠道
			this.initAnChannel(resInfo, datiDTO);
			// 提交答案时，再次通过cookie配置微信用户信息，初始化微信答题授权的微信用户信息
			this.initPageWxAuthFromCookie(resInfo, req, datiDTO);
			// 提交答案时进行检查，是否绕过微信进行提交
			datiService.validWxCfgDatiSubmit(resInfo, req, datiDTO);
			// 验证微信用户答题数量控制
			datiService.validWxCount(resInfo, datiDTO);
		}
		// 版本号验证
		datiService.validAnVersion(resInfo, datiDTO);
		datiService.anDateCtl(resInfo, datiDTO);
		// -----针对填写答案部分处理开始-------
		// 变量值转数据库字段对应值
		datiService.initVarAn2Field(resInfo, datiDTO);
		if (!datiDTO.isRtn()) {// 返回上页，不需要验证配额
			// 验证配额
			datiService.validQuota(resInfo, datiDTO);
		}
		// 主要做答题存储方面的
		datiStoreService.datiStore(resInfo, datiDTO);

		if (!resInfo.isHasError()) {
			Map<String, Object> map = new HashMap();
			map.put("anId", datiDTO.getAnId());
			resInfo.setRes(map);
		}
		return resInfo;
	}

	/**
	 * 
	 * <b>描述:答题时需要的问卷结构，问卷、问题等</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/datiSurveyQuessData")
	public ResInfo datiSurveyQuessData(HttpServletRequest req, HttpServletResponse res, @RequestParam String surveyId) {
		ResInfo resInfo = new ResInfo();

		DatiDTO datiDTO = new DatiDTO();
		datiDTO.setSurveyId(surveyId);
		// 初始化浏览器代理信息
		this.initUserAgent(req, datiDTO);
		Map<String, Object> resMap = new HashMap();

		resMap = datiService.datiSurveyDataByCache(resInfo, datiDTO);
		
		datiService.anDateCtl(resInfo, datiDTO);
		// 如果有问卷cookie，初始化答题的cookie
		this.initSurveyCookieFromRequest(resInfo, req, datiDTO);
		// 初始化微信答题授权的微信用户信息
		this.initPageWxAuthFromCookie(resInfo, req, datiDTO);
		// log.info(JSON.toJSONString(datiDTO.getWxUser()));

		if (datiDTO.getCookie() == null) {// 没有答过题
			// 验证答题回收数
			datiService.calcCheckAnSize(resInfo, datiDTO);
			// 初始化ip地址和通过ip得到区域信息
			this.initIpAndCity(resInfo, req, datiDTO);
			// 验证cookie和ip的限制答题数量是否超限制
			datiService.validIpCount(resInfo, datiDTO);
			// datiService.validCookieCount(datiDTO);
			// 没有问卷cookie，给浏览器发送答题的cookie
			this.setSurveyCookie2Browser(resInfo, res, datiDTO.getSurveyId());
		}
		// 每次请求问卷数据检查是否要求微信浏览器,以及是否要授权，如果datiDTO有微信用户信息，不用微信授权
		datiService.checkWxCfgDatiStart(resInfo, datiDTO);
		SurveyMO survey = datiDTO.getSurvey();
		WxCfg  wxCfg = survey.getWxCfg();
		if(survey.isRequiredWx()&&wxCfg.isLogin()&&wxCfg.isUpdate()){
			datiService.getHisAn(req,surveyId,resMap);
		}
		resInfo.setRes(resMap);
		if (resInfo.isHasError()) {
			resInfo.setStatus(ResInfo.TIP);// 第一次展示错误，都已提示状态
		}

		return resInfo;
	}

}
