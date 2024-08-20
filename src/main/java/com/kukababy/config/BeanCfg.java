package com.kukababy.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kukababy.filter.AllowFilter;
import com.kukababy.filter.LoginFilter;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年4月27日 上午11:08:39
 */
@Configuration
public class BeanCfg {

	@Bean
	public Filter loginFilter() {
		return new LoginFilter();
	}

	@Bean
	public Filter allowFilter() {
		return new AllowFilter();
	}

	@Bean
	public FilterRegistrationBean allowFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(allowFilter());
		registration.addUrlPatterns("/*");

		return registration;
	}

	@Bean
	public FilterRegistrationBean loginFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();

		registration.setFilter(loginFilter());
		registration.addUrlPatterns("/design/*");
		registration.addUrlPatterns("/data/*");
		// registration.addUrlPatterns("*.html");
		// registration.setOrder(10);
		return registration;
	}

}
