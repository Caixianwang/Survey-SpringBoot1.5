/**
 * 
 */
package com.kukababy.entity.mongo;

import org.springframework.data.annotation.Id;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年7月15日 下午6:55:50
 */

public class DatiBO {
	@Id
	private String anId;
	private String surveyId;
	private String ip;
	private String cookie;
	private String wxOpenid ;
	/**
	 * @return the anId
	 */
	public String getAnId() {
		return anId;
	}
	/**
	 * @param anId the anId to set
	 */
	public void setAnId(String anId) {
		this.anId = anId;
	}
	/**
	 * @return the surveyId
	 */
	public String getSurveyId() {
		return surveyId;
	}
	/**
	 * @param surveyId the surveyId to set
	 */
	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the cookie
	 */
	public String getCookie() {
		return cookie;
	}
	/**
	 * @param cookie the cookie to set
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	/**
	 * @return the wxOpenid
	 */
	public String getWxOpenid() {
		return wxOpenid;
	}
	/**
	 * @param wxOpenid the wxOpenid to set
	 */
	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}
	
}
