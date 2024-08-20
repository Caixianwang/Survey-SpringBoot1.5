/**
 * 
 */
package com.kukababy.entity.mongo.bo;

import java.util.ArrayList;
import java.util.Date;
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
public class QuotaGroupBO {
	private String name;
	private Date created ;
	private List<QuotaDetailBO> details = new ArrayList();
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
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	/**
	 * @return the details
	 */
	public List<QuotaDetailBO> getDetails() {
		return details;
	}
	/**
	 * @param details the details to set
	 */
	public void setDetails(List<QuotaDetailBO> details) {
		this.details = details;
	}
	

}	
