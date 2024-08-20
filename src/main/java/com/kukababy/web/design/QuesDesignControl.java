package com.kukababy.web.design;

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
import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.pager.Page;
import com.kukababy.pager.Pager;
import com.kukababy.res.ResInfo;
import com.kukababy.service.design.QuesDesignService;
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
public class QuesDesignControl extends BaseControl {

	private static final Logger log = LoggerFactory.getLogger(QuesDesignControl.class);

	@Autowired(required = true)
	private QuesDesignService quesDesignService;
	
	@RequestMapping(value = "/getCopyQues")
	public ResInfo getCopyQues(@RequestParam String quesKey) {
		ResInfo res = new ResInfo();
		Map<String,Object> ques = quesDesignService.getCopyQues(quesKey);
		res.setRes(ques);
		return res;
	}
	
	@RequestMapping(value = "/delQuesLib", method = RequestMethod.GET)
	public ResInfo delQuesLib(@RequestParam String quesKey) {
		ResInfo resInfo = new ResInfo();
		quesDesignService.delQuesLib(quesKey);
		return resInfo;
	}
	
	@RequestMapping(value = "/getQuesLib")
	public ResInfo getQuesLib(@RequestParam String quesKey,@RequestParam String ref) {
		ResInfo res = new ResInfo();
		Map<String,Object> ques = quesDesignService.getQuesLib(quesKey,ref);
		res.setRes(ques);
		return res;
	}
	
	@RequestMapping(value = "/quesLibList", method = RequestMethod.POST)
	public ResInfo quesLibList(@RequestBody Pager pager,HttpServletRequest request) {
		log.info("quesLibList=" + JSON.toJSONString(pager));
		ResInfo resInfo = new ResInfo();
		String share = request.getParameter("share");
		quesDesignService.quesLibList(pager,share);
		resInfo.setRes(new Page(pager.getTotal(), pager.getRows()));
		return resInfo;
	}
	
	@RequestMapping(value = "/queryQues")
	public ResInfo queryQues(@RequestParam String surveyId) {
		ResInfo res = new ResInfo();
		List<Map<String,Object>> quess = quesDesignService.queryQues(surveyId);
		res.setRes(quess);
		return res;
	}

	@RequestMapping(value = "/editQues", method = RequestMethod.POST)
	public ResInfo editQues(@RequestBody Map<String, Object> params) {
		ResInfo res = new ResInfo();
		log.info("editQues=" + JSON.toJSONString(params));
		quesDesignService.editQues(params,"m_ques");
		//params.put("quesKey", params.get("_id"));
		//params.remove("_id");
		res.setRes(params);
		return res;
	}
	
	@RequestMapping(value = "/editQuesLib", method = RequestMethod.POST)
	public ResInfo editQuesLib(@RequestBody Map<String, Object> params) {
		ResInfo res = new ResInfo();
		log.info("editQuesLib=" + JSON.toJSONString(params));
		quesDesignService.editQues(params,"m_queslib");
		//params.put("quesKey", params.get("_id"));
		//params.remove("_id");
		res.setRes(params);
		return res;
	}

}
