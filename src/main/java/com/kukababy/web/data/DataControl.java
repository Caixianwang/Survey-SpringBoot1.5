package com.kukababy.web.data;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.zxing.WriterException;
import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.pager.Page;
import com.kukababy.pager.Pager;
import com.kukababy.res.ResInfo;
import com.kukababy.service.data.DataService;
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
@RequestMapping("/data")
public class DataControl extends BaseControl {

	private static final Logger log = LoggerFactory.getLogger(DataControl.class);

	@Autowired(required = true)
	private DataService dataService;
	
	@RequestMapping(value = "/delSample", method = RequestMethod.GET)
	public ResInfo delSample(HttpServletRequest request) {

		ResInfo resInfo = new ResInfo();
		String surveyId = request.getParameter("surveyId");
		String anId = request.getParameter("anId");
		dataService.delSample(anId, surveyId);
		return resInfo;
	}
	
	@RequestMapping(value = "/queryData01", method = RequestMethod.POST)
	public ResInfo surveyList(@RequestBody Pager pager,HttpServletRequest request) {
		log.info("surveyList=" + JSON.toJSONString(pager));
		ResInfo resInfo = new ResInfo();
		String surveyId = request.getParameter("surveyId");
		Page page = dataService.queryData01(pager, surveyId);
		resInfo.setRes(page);
		return resInfo;
	}
	
	@RequestMapping(value = "/getDataHeader01", method = RequestMethod.GET)
	public ResInfo getDataHeader01(HttpServletRequest request){
		ResInfo res = new ResInfo();
		String surveyId = request.getParameter("surveyId");
		
		Map<String, Object> map = dataService.getDataHeader01(surveyId);
		res.setRes(map);
		return res;
		
	}

	@RequestMapping(value = "/download01", method = RequestMethod.POST)
	public void download01(@RequestBody Pager pager,HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, WriterException, IOException {
		OutputStream os = response.getOutputStream();
		log.info(JSON.toJSONString(pager));
		String surveyId = request.getParameter("surveyId");
		HSSFWorkbook workbook = dataService.exportData01(pager,surveyId);
		this.downloadXlsFile(response, workbook,surveyId);
	}

	
}
