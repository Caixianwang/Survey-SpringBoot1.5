/**
 * 
 */
package com.kukababy.entity.vo;

import java.util.Date;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian.wang@ezcharting.cn
 * @date 2017年7月15日 下午6:55:50
 */

public class UserVO  {
	private String key;
	private String userId;
	private String userName ;
	private String wxNickname;
	private String passwd;
	private String captcha;
	private boolean hasPd = true;
	private boolean wxUse = true;
	private String other;
	/**
	 * @return the captcha
	 */
	public String getCaptcha() {
		return captcha;
	}
	/**
	 * @param captcha the captcha to set
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}
	/**
	 * @param passwd the passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	/**
	 * @return the hasPd
	 */
	public boolean isHasPd() {
		return hasPd;
	}
	/**
	 * @param hasPd the hasPd to set
	 */
	public void setHasPd(boolean hasPd) {
		this.hasPd = hasPd;
	}
	/**
	 * @return the wxNickname
	 */
	public String getWxNickname() {
		return wxNickname;
	}
	/**
	 * @param wxNickname the wxNickname to set
	 */
	public void setWxNickname(String wxNickname) {
		this.wxNickname = wxNickname;
	}
	/**
	 * @return the wxUse
	 */
	public boolean isWxUse() {
		return wxUse;
	}
	/**
	 * @param wxUse the wxUse to set
	 */
	public void setWxUse(boolean wxUse) {
		this.wxUse = wxUse;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the other
	 */
	public String getOther() {
		return other;
	}
	/**
	 * @param other the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}
	
	
	
}
