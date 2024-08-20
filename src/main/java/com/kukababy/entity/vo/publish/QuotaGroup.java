/**
 * 
 */
package com.kukababy.entity.vo.publish;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

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
public class QuotaGroup {
	private String name;
	private List<QuotaDetail> details = new ArrayList();
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the details
	 */
	public List<QuotaDetail> getDetails() {
		return details;
	}
	/**
	 * @param details the details to set
	 */
	public void setDetails(List<QuotaDetail> details) {
		this.details = details;
	}

	
	
}
