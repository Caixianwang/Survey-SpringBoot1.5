package com.kukababy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <b>描述</b>:
 * <blockquote>
 * <pre>
 * </pre>
 * </blockquote>
 * @author caixian_wang@sina.com
 * @date 2017年4月27日 上午9:10:58
 */
@Configuration
public class MvcCfg extends WebMvcConfigurerAdapter {  
	private static final Logger log = LoggerFactory.getLogger(MvcCfg.class);
	/**
    @Override  
    public void addViewControllers(ViewControllerRegistry registry) {  
        registry.addViewController("/error").setViewName("error.html");  
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);  
    }*/

    @Override  
    public void configurePathMatch(PathMatchConfigurer configurer) {  
        super.configurePathMatch(configurer);  
        configurer.setUseSuffixPatternMatch(false);  
    }
}
