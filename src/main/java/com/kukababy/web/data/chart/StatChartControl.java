package com.kukababy.web.data.chart;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.kukababy.pager.FilterEL;
import com.kukababy.pager.MongoWhere;
import com.kukababy.pager.Pager;
import com.kukababy.res.ResInfo;
import com.kukababy.service.data.StatChartService;
import com.kukababy.web.base.BaseControl;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

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
@RequestMapping("/data/chart")
public class StatChartControl extends BaseControl {

	private static final Logger log = LoggerFactory.getLogger(StatChartControl.class);

	@Autowired(required = true)
	private StatChartService statChartService;
	
	@RequestMapping(value = "/dataCount", method = RequestMethod.POST)
	public ResInfo dataCount(@RequestBody Pager pager,HttpServletRequest request) {
		log.info("dataCount=" + JSON.toJSONString(pager));
		ResInfo resInfo = new ResInfo();
		String surveyId = request.getParameter("surveyId");
		long count = statChartService.getCount(pager, surveyId);
		resInfo.setRes(count);
		return resInfo;
	}
	
	@RequestMapping(value = "/endData", method = RequestMethod.POST)
	public ResInfo endData(@RequestBody Map<String,Object> reqMap) {
		//log.info("inputScrollData=" + JSON.toJSONString(reqMap));
		ResInfo resInfo = new ResInfo();
		
		Map<String, Object> quesMap= (Map)reqMap.get("ques");
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);

		BasicDBObject filters = MongoWhere.getWheres(pager);
		Map<String, Object> res = statChartService.endData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	
	@RequestMapping(value = "/mulGoodsData", method = RequestMethod.POST)
	public ResInfo mulGoodsData(@RequestBody Map<String,Object> reqMap) {
		//log.info("inputScrollData=" + JSON.toJSONString(reqMap));
		ResInfo resInfo = new ResInfo();
		
		Map<String, Object> quesMap= (Map)reqMap.get("ques");

		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);

		BasicDBObject filters = MongoWhere.getWheres(pager);
		Map<String, Object> res = statChartService.mulGoodsData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	
	@RequestMapping(value = "/radioCascaderData", method = RequestMethod.POST)
	public ResInfo radioCascaderData(@RequestBody Map<String,Object> reqMap) {
		//log.info("inputScrollData=" + JSON.toJSONString(reqMap));
		ResInfo resInfo = new ResInfo();
		
		Map<String, Object> quesMap= (Map)reqMap.get("ques");
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);

		BasicDBObject filters = MongoWhere.getWheres(pager);
		List<DBObject> list = statChartService.radioCascaderData(quesMap, filters);
		resInfo.setRes(list);
		return resInfo;
	}
	
	@RequestMapping(value = "/matrixSelectData", method = RequestMethod.POST)
	public ResInfo matrixSelectData(@RequestBody Map<String,Object> reqMap) {
		//log.info("inputScrollData=" + JSON.toJSONString(reqMap));
		ResInfo resInfo = new ResInfo();
		
		Map<String, Object> quesMap= (Map)reqMap.get("ques");
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);

		BasicDBObject filters = MongoWhere.getWheres(pager);
		Map<String, Object> res = statChartService.matrixSelectData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	
	@RequestMapping(value = "/mulSortData", method = RequestMethod.POST)
	public ResInfo mulSortData(@RequestBody Map<String,Object> reqMap) {
		//log.info("inputScrollData=" + JSON.toJSONString(reqMap));
		ResInfo resInfo = new ResInfo();
		
		Map<String, Object> quesMap= (Map)reqMap.get("ques");
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);

		BasicDBObject filters = MongoWhere.getWheres(pager);
		Map<String, Object> res = statChartService.mulSortData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	
	@RequestMapping(value = "/inputScrollData", method = RequestMethod.POST)
	public ResInfo inputScrollData(@RequestBody Map<String,Object> reqMap) {
		//log.info("inputScrollData=" + JSON.toJSONString(reqMap));
		
		ResInfo resInfo = new ResInfo();
		
		Map<String, Object> quesMap= (Map)reqMap.get("ques");
		//String tblName = statChartService.anTblName((String)quesMap.get("surveyId"));
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);

		BasicDBObject filters = MongoWhere.getWheres(pager);
		Map<String, Object> res = statChartService.inputScrollData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	
	@RequestMapping(value = "/matrixCheckboxData", method = RequestMethod.POST)
	public ResInfo matrixCheckboxData(@RequestBody Map<String,Object> reqMap) {
		//log.info("matrixCheckboxData=" + JSON.toJSONString(reqMap));
		
		ResInfo resInfo = new ResInfo();
		Map<String, Object> quesMap= (Map)reqMap.get("ques");//
		//String tblName = statChartService.anTblName((String)quesMap.get("surveyId"));
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);

		BasicDBObject filters = MongoWhere.getWheres(pager);
		Map<String, Object> res = statChartService.matrixCheckboxData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	
	@RequestMapping(value = "/matrixRadioData", method = RequestMethod.POST)
	public ResInfo matrixRadioData(@RequestBody Map<String,Object> reqMap) {
		//log.info("matrixRadioData=" + JSON.toJSONString(reqMap));
		
		ResInfo resInfo = new ResInfo();
		Map<String, Object> quesMap= (Map)reqMap.get("ques");
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);
		BasicDBObject filters = MongoWhere.getWheres(pager);
		Map<String, Object> res = statChartService.matrixRadioData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	@RequestMapping(value = "/checkboxData", method = RequestMethod.POST)
	public ResInfo checkboxData(@RequestBody Map<String,Object> reqMap) {
		//log.info("checkboxData=" + JSON.toJSONString(reqMap));
		
		ResInfo resInfo = new ResInfo();
		Map<String, Object> quesMap= (Map)reqMap.get("ques");

		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);

		BasicDBObject filters = MongoWhere.getWheres(pager);
		Map<String, Object> res = statChartService.checkboxData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	
	@RequestMapping(value = "/radioRateData", method = RequestMethod.POST)
	public ResInfo radioRateData(@RequestBody Map<String,Object> reqMap) {
		log.info("radioRateData=" + JSON.toJSONString(reqMap));
		
		ResInfo resInfo = new ResInfo();
		Map<String, Object> quesMap= (Map)reqMap.get("ques");
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);

		BasicDBObject filters = MongoWhere.getWheres(pager);
		Map<String, Object> res = statChartService.radioRateData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	@RequestMapping(value = "/radioData", method = RequestMethod.POST)
	public ResInfo radioData(@RequestBody Map<String,Object> reqMap) {
		log.info("radioData=" + JSON.toJSONString(reqMap));
		
		ResInfo resInfo = new ResInfo();
		Map<String, Object> quesMap= (Map)reqMap.get("ques");

		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);
		BasicDBObject filters = MongoWhere.getWheres(pager);
		
		Map<String, Object> res = statChartService.radioData(quesMap, filters);
		resInfo.setRes(res);
		return resInfo;
	}
	
	@RequestMapping(value = "/getSurveyAndQuess", method = RequestMethod.GET)
	public ResInfo getSurveyAndQuess(@RequestParam String surveyId) {
		ResInfo resInfo = new ResInfo();
		Map<String, Object> res = statChartService.getSurveyAndQuess(surveyId);
		resInfo.setRes(res);
		return resInfo;
	}
	
	
}
