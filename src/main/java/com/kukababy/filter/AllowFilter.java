package com.kukababy.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian.wang@ezcharting.cn
 * @date 2017年4月27日 上午11:09:51
 */
public class AllowFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(AllowFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("init IndexFilter");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		// log.info(request.getRequestURL().toString());
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("Access-Control-Allow-Origin", "http://www.kukababy.com");
		response.setHeader("Access-Control-Allow-Headers", "token,Origin, X-Requested-With, Content-Type, Accept");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT,DELETE,OPTION");
		// response.setHeader("Access-Control-Max-Age","3600");
		// filterChain.doFilter(servletRequest,servletResponse);
		filterChain.doFilter(new XssWraper(request), servletResponse);

	}

	@Override
	public void destroy() {

	}
}
