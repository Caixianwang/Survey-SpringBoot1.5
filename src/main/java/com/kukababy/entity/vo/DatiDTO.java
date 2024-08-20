/**
 * 
 */
package com.kukababy.entity.vo;

import java.util.List;
import java.util.Map;

import com.kukababy.entity.mongo.SurveyMO;
import com.kukababy.entity.vo.publish.QuotaGroup;
import com.kukababy.utils.Consts;

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

public class DatiDTO {
	private String anId;
	private String surveyId;
	private SurveyMO survey;
	/**
	 * 微信答题是否跳过
	 */
	private boolean wxSkip = false;
	/**
	 * 提交是否返回上页
	 */
	private boolean rtn = false ;
	/**
	 * 微信用户信息
	 */
	private WxUser wxUser ;
	private String ip;
	private String cookie;
	/**
	 * 通过IP计算的省份
	 */
	private String province;
	/**
	 * 通过IP计算的城市
	 */
	private String city;
	
	/**
	 * 浏览器代理的信息
	 */
	private String userAgent ;
	/**
	 * 答题渠道，微信答题0、直接链接1
	 */
	private String anChannel = Consts.anChannel0;
	/**
	 *客户端的变量答案
	 */
	private Map<String, Object> currVarAnMap;
	/**
	 * 所有答案
	 */
	private Map<String, Object> varAnMap;
	/**
	 * 客户端的变量答案转换成数据库对应字段答案
	 */
	private Map<String, Object> currFieldAnMap;
	/**
	 * 配额情况
	 */
	private List<QuotaGroup> quotaGroups;
	/**
	 * 变量名和字段名的映射
	 */
	private Map<String, String> var2FieldMap;
	/**
	 * 实际样本数
	 */
	private long realAnSize;

	/**
	 * 对应问卷在redis的键
	 */
	private String redisKey;
	/**
	 * 是否是回退提交，回退时，清除当前的提交答案
	 */
	private boolean back =false;
	/**
	 * 发布的版本
	 */
	private long version = 0;

	/**
	 * @return the survey
	 */
	public SurveyMO getSurvey() {
		return survey;
	}

	/**
	 * @param survey
	 *            the survey to set
	 */
	public void setSurvey(SurveyMO survey) {
		this.survey = survey;
	}

	/**
	 * @return the anId
	 */
	public String getAnId() {
		return anId;
	}

	/**
	 * @param anId
	 *            the anId to set
	 */
	public void setAnId(String anId) {
		this.anId = anId;
	}

	/**
	 * @return the currVarAnMap
	 */
	public Map<String, Object> getCurrVarAnMap() {
		return currVarAnMap;
	}

	/**
	 * @param currVarAnMap
	 *            the currVarAnMap to set
	 */
	public void setCurrVarAnMap(Map<String, Object> currVarAnMap) {
		this.currVarAnMap = currVarAnMap;
	}

	/**
	 * @return the currFieldAnMap
	 */
	public Map<String, Object> getCurrFieldAnMap() {
		return currFieldAnMap;
	}

	/**
	 * @param currFieldAnMap
	 *            the currFieldAnMap to set
	 */
	public void setCurrFieldAnMap(Map<String, Object> currFieldAnMap) {
		this.currFieldAnMap = currFieldAnMap;
	}


	/**
	 * @return the back
	 */
	public boolean isBack() {
		return back;
	}

	/**
	 * @param back
	 *            the back to set
	 */
	public void setBack(boolean back) {
		this.back = back;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the surveyId
	 */
	public String getSurveyId() {
		return surveyId;
	}

	/**
	 * @param surveyId
	 *            the surveyId to set
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
	 * @param ip
	 *            the ip to set
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
	 * @param cookie
	 *            the cookie to set
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	/**
	 * @return the quotaGroups
	 */
	public List<QuotaGroup> getQuotaGroups() {
		return quotaGroups;
	}

	/**
	 * @param quotaGroups
	 *            the quotaGroups to set
	 */
	public void setQuotaGroups(List<QuotaGroup> quotaGroups) {
		this.quotaGroups = quotaGroups;
	}

	/**
	 * @return the var2FieldMap
	 */
	public Map<String, String> getVar2FieldMap() {
		return var2FieldMap;
	}

	/**
	 * @param var2FieldMap
	 *            the var2FieldMap to set
	 */
	public void setVar2FieldMap(Map<String, String> var2FieldMap) {
		this.var2FieldMap = var2FieldMap;
	}

	/**
	 * @return the redisKey
	 */
	public String getRedisKey() {
		return redisKey;
	}

	/**
	 * @param redisKey
	 *            the redisKey to set
	 */
	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}

	/**
	 * @return the realAnSize
	 */
	public long getRealAnSize() {
		return realAnSize;
	}

	/**
	 * @param realAnSize
	 *            the realAnSize to set
	 */
	public void setRealAnSize(long realAnSize) {
		this.realAnSize = realAnSize;
	}

	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return the wxUser
	 */
	public WxUser getWxUser() {
		return wxUser;
	}

	/**
	 * @param wxUser the wxUser to set
	 */
	public void setWxUser(WxUser wxUser) {
		this.wxUser = wxUser;
	}

	/**
	 * @return the anChannel
	 */
	public String getAnChannel() {
		return anChannel;
	}

	/**
	 * @param anChannel the anChannel to set
	 */
	public void setAnChannel(String anChannel) {
		this.anChannel = anChannel;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return the wxSkip
	 */
	public boolean isWxSkip() {
		return wxSkip;
	}

	/**
	 * @param wxSkip the wxSkip to set
	 */
	public void setWxSkip(boolean wxSkip) {
		this.wxSkip = wxSkip;
	}

	/**
	 * @return the rtn
	 */
	public boolean isRtn() {
		return rtn;
	}

	/**
	 * @param rtn the rtn to set
	 */
	public void setRtn(boolean rtn) {
		this.rtn = rtn;
	}

	/**
	 * @return the varAnMap
	 */
	public Map<String, Object> getVarAnMap() {
		return varAnMap;
	}

	/**
	 * @param varAnMap the varAnMap to set
	 */
	public void setVarAnMap(Map<String, Object> varAnMap) {
		this.varAnMap = varAnMap;
	}

}
