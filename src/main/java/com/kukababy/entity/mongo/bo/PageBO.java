/**
 * 
 */
package com.kukababy.entity.mongo.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年8月7日 下午2:52:23
 */
public class PageBO {
	private String pageId;
	/**
	 * 答题时长（秒）
	 */
	private int duration = 0;
	
	private List<String> quesIds = new ArrayList();
	/**
	 * @return the pageId
	 */
	public String getPageId() {
		return pageId;
	}
	/**
	 * @param pageId the pageId to set
	 */
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	/**
	 * @return the quesIds
	 */
	public List<String> getQuesIds() {
		return quesIds;
	}
	/**
	 * @param quesIds the quesIds to set
	 */
	public void setQuesIds(List<String> quesIds) {
		this.quesIds = quesIds;
	}
	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	
	
}
