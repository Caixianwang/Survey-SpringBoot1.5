package com.kukababy.web.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kukababy.key.KeyUtils;

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

public class BaseControl {

	private static final Logger log = LoggerFactory.getLogger(BaseControl.class);

	protected void downloadFile(HttpServletResponse response, byte[] bt, String ext, String fileName) throws IOException {
		// response.setContentType(contentType);
		if (fileName == null) {
			fileName = KeyUtils.getInstance().getUUID("fd");
		}
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("utf-8"), "ISO8859_1") + ext + "\"");
		response.setHeader("filename",new String(fileName.getBytes("utf-8"), "ISO8859_1") + ext);
		OutputStream os = response.getOutputStream();
		os.write(bt);
		os.flush();
		os.close();
	}
	
	protected void downloadXlsFile(HttpServletResponse response, HSSFWorkbook work,String fileName) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		work.write(bos);
		this.downloadFile(response, bos.toByteArray(), ".xls",fileName);
	}

}
