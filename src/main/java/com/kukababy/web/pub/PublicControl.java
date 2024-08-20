package com.kukababy.web.pub;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.google.zxing.WriterException;
import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.entity.vo.UserVO;
import com.kukababy.pager.FilterEL;
import com.kukababy.pager.MongoWhere;
import com.kukababy.pager.Page;
import com.kukababy.pager.Pager;
import com.kukababy.res.ResInfo;
import com.kukababy.service.LoginService;
import com.kukababy.service.data.StatChartService;
import com.kukababy.service.design.SurveyDesignService;
import com.kukababy.utils.PropUtil;
import com.kukababy.utils.QRCodeUtil;
import com.kukababy.utils.RedisConsts;
import com.kukababy.web.base.BaseControl;
import com.mongodb.BasicDBObject;

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
@RequestMapping("/pub")
public class PublicControl extends BaseControl {

	private static final Logger log = LoggerFactory.getLogger(PublicControl.class);
	
	@Autowired(required = true)
	private SurveyDesignService surveyDesignService;
	@Autowired
	private LoginService loginService;
	@Autowired(required = true)
	private StatChartService statChartService;
	
	@RequestMapping(value = "/test")
	public ModelAndView test() {
		Map<String, Object> model = new HashMap();
		model.put("name", "小白问卷");
		return new ModelAndView("scanSuccess", model);
	}
	
	@RequestMapping(value = "/checkboxRes", method = RequestMethod.POST)
	public ResInfo checkboxRes(@RequestBody Map<String,Object> reqMap) {
		log.info("checkboxRes=" + JSON.toJSONString(reqMap));
		
		ResInfo resInfo = new ResInfo();
		String quesKey= (String)reqMap.get("quesKey");
		String surveyId= (String)reqMap.get("surveyId");
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);
		BasicDBObject filters = MongoWhere.getWheres(pager);
		
		Map<String, Object> voteRes = statChartService.checkboxData(quesKey, filters);
		long total = statChartService.getCount(pager, surveyId);
		Map<String,Object> res = new HashMap();
		res.put("total", total);
		res.put("voteRes", voteRes);
		resInfo.setRes(res);
		return resInfo;
	}
	
	@RequestMapping(value = "/radioRes", method = RequestMethod.POST)
	public ResInfo radioRes(@RequestBody Map<String,Object> reqMap) {
		log.info("radioRes=" + JSON.toJSONString(reqMap));
		
		ResInfo resInfo = new ResInfo();
		String quesKey= (String)reqMap.get("quesKey");
		String surveyId= (String)reqMap.get("surveyId");
		Pager pager = JSON.parseObject(JSON.toJSONString(reqMap.get("pager")),Pager.class);
		BasicDBObject filters = MongoWhere.getWheres(pager);
		
		Map<String, Object> voteRes = statChartService.radioData(quesKey, filters);
		long total = statChartService.getCount(pager, surveyId);
		Map<String,Object> res = new HashMap();
		res.put("total", total);
		res.put("voteRes", voteRes);
		resInfo.setRes(res);
		return resInfo;
	}

	
	@RequestMapping(value = "/userLogin")
	public ResInfo userLogin(HttpServletRequest request) {
		ResInfo resInfo = new ResInfo();
		resInfo.setRes("N");
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(RedisConsts.cookieKukaName)) {
					String key = cookie.getValue();
					UserVO user = loginService.getUserOfRedis(key);
					if (user!= null) {// 更新cookie时间
						resInfo.setRes("Y");
					}
					break;
				}
			}
		}
		
		return resInfo;
	}
	
	@RequestMapping(value = "/pubSurveyList", method = RequestMethod.POST)
	public ResInfo pubSurveyList(@RequestBody Pager<SurveyMO> pager) {
		log.info("pubSurveyList=" + JSON.toJSONString(pager));
		ResInfo resInfo = new ResInfo();
		surveyDesignService.pubSurveyList(pager);
		resInfo.setRes(new Page(pager.getTotal(), pager.getRows()));
		return resInfo;
	}


	@RequestMapping(value = "/qrcodeImg", method = RequestMethod.GET)
	public void qrcodeImg(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, WriterException, IOException {
		OutputStream os = response.getOutputStream();
		BufferedImage bufImage = getQrcodeImg(request, response);
		if (request.getParameter("d") != null) {// 需要下载
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bufImage, "PNG", bos);
			String qrcodeName = "qrcode";
			this.downloadFile(response, bos.toByteArray(), ".png", qrcodeName);
		} else {
			ImageIO.write(bufImage, "PNG", os);
		}
	}

	private BufferedImage getQrcodeImg(HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
		String surveyId = request.getParameter("surveyId");
		
		int qrSize = 150;
		if (request.getParameter("qrSize") != null) {
			qrSize = Integer.valueOf(request.getParameter("qrSize"));
		}

		String imgUrl = request.getParameter("imgUrl");
		String datiDomain = PropUtil.get("dati.domain");
		String content = datiDomain + "/dati.html?sd=" + surveyId;
		
		log.info(content);
		if (StringUtils.isBlank(imgUrl)) {
			String basePath = request.getSession().getServletContext().getRealPath("");
			imgUrl = basePath + "resource/img/logo.png";
		}

		BufferedImage bufImage = QRCodeUtil.createImage(content, imgUrl, qrSize, true);
		return bufImage;
	}


}
