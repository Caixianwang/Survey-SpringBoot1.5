package com.kukababy.web.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.kukababy.res.ResInfo;
import com.kukababy.utils.PropUtil;

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
@RequestMapping("/file")
public class FileControl {

	private static final Logger log = LoggerFactory.getLogger(FileControl.class);
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/ckUploadFile", method = RequestMethod.POST)
	public void uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("upload") MultipartFile file) throws IOException {
		String fineName = file.getOriginalFilename() ;
		String uploadUrl = PropUtil.get("upload.api");
		ByteArrayResource arrayResource = new ByteArrayResource(file.getBytes()) {
			@Override
			public String getFilename() throws IllegalStateException {
				return fineName;
			}
		};

		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("file", arrayResource);

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);
		ResInfo resInfo = restTemplate.exchange(uploadUrl, HttpMethod.POST, httpEntity, ResInfo.class).getBody();
		log.info(JSON.toJSONString(resInfo));
		String fileUrl = (String)((Map<String,Object>)resInfo.getRes()).get("localUrl");
		String callback = request.getParameter("CKEditorFuncNum");
		PrintWriter writer = response.getWriter();
		writer.println("<script type=\"text/javascript\">");
		writer.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + fileUrl + "',''" + ")");
		writer.println("</script>");
		writer.flush();
		writer.close();
	}

	@RequestMapping(value = "/ckeditorFileBrowse", method = RequestMethod.POST)
	public ResInfo ckeditorFileBrowse(HttpServletRequest request) throws IOException {
		String basePath = request.getSession().getServletContext().getRealPath("");
		basePath = basePath + "images";
		File root = new File(basePath);
		ResInfo resInfo = new ResInfo();
		List<String> names = new ArrayList();
		File[] files = root.listFiles();
		if (files.length > 0) {
			for (File file : files) {
				names.add(file.getName());
			}
		}
		resInfo.setRes(names);
		log.info(JSON.toJSONString(names));
		return resInfo;
	}

}
