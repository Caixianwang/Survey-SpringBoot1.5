/**
 * 
 */
package com.kukababy.service.comm;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <b>描述:</b>:
 * <blockquote>
 * <pre>
 * </pre>
 * </blockquote>
 * @author caixian_wang@sina.com
 * @date 2018年11月30日 上午11:01:58
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContextParam) throws BeansException {
        applicationContext=applicationContextParam;
    }
    public static Object getObject(String id) {
        Object object = null;
        object = applicationContext.getBean(id);
        return object;
    }
    public static <T> T getObject(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static Object getBean(String tClass) {
        return applicationContext.getBean(tClass);
    }

    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }
}
