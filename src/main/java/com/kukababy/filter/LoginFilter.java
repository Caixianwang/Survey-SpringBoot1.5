package com.kukababy.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kukababy.common.ThreadCache;
import com.kukababy.entity.vo.UserVO;
import com.kukababy.service.LoginService;
import com.kukababy.utils.RedisConsts;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年4月27日 上午11:09:51
 */
public class LoginFilter extends BaseFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);
	@Autowired
	private LoginService loginService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
	
		sessionHandle(filterChain, request, response);
	}

	private void sessionHandle(FilterChain filterChain, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		UserVO user = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(RedisConsts.cookieKukaName)) {
					String key = cookie.getValue();
					user = loginService.getUserOfRedis(key);
					if (user != null) {// 更新cookie时间
						loginService.updateUserTimeoutOfRedis(key);
						ThreadCache.setUser(user);//线程缓层当前用户
					}
					break;
				}
			}
		}
		if (user != null) {
			filterChain.doFilter(request, response);
		} else {
			writeExpirie(response,"cookie expiried");
		}
	}

	@Override
	public void destroy() {

	}
}
