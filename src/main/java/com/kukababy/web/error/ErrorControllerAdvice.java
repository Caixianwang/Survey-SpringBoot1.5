package com.kukababy.web.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kukababy.res.ResInfo;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年12月5日 上午9:40:43
 */
@ControllerAdvice
public class ErrorControllerAdvice {

	private static final Logger log = LoggerFactory.getLogger(ErrorControllerAdvice.class);

	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public ResInfo errorHandler(Exception ex) {
		ex.printStackTrace();
		log.error(ex.toString());
		ResInfo res = new ResInfo();
		res.setMsg(ex.toString());
		res.setStatus(ResInfo.FAIL);
		return res;
	}

}
