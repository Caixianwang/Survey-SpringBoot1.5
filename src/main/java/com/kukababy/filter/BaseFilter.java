package com.kukababy.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
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
public class BaseFilter {
	
	protected void writeExpirie(HttpServletResponse response,String msg) throws IOException {
		ResInfo info = new ResInfo();
		if(msg==null){
			info.setMsg("Token expiried");
		}else{
			info.setMsg(msg);
		}
		
		info.setStatus(ResInfo.OUT);
		writePage(response,JSON.toJSONString(info), true);
	}

	protected void writePage(HttpServletResponse response, String content, boolean json) throws IOException {
		if (json) {
			response.setContentType("application/json;charset=utf-8");
		} else {
			response.setContentType("text/html;charset=utf-8");
		}
		//PrintWriter writer = response.getWriter();
		//writer.write(content);
		//writer.flush();
		//writer.close();
		response.getWriter().print(content);
	}

}
