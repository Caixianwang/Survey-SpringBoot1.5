/**
 * 
 */
package com.kukababy.entity.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kukababy.entity.mongo.bo.PageBO;
import com.kukababy.entity.mongo.bo.QuotaGroupBO;
import com.kukababy.entity.mongo.bo.SurveyCfgBO;
import com.kukababy.entity.vo.WxCfg;
import com.kukababy.utils.Consts;

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
@Document(collection = "m_survey")
public class SurveyMO {
	public static final String TABLE_NAME = "m_survey";
	@Id
	private String surveyId;
	private String title;
	
	private String userId;
	private String pub = Consts.NO;

	private String publishState = Consts.NO;
	private Date publishTime;
	
	private String template= Consts.NO;
	private String share = Consts.NO;
	
	private boolean dateCtl = false ;
	private Date startDate ;
	private Date endDate ;
	private String type ="0";//调研
	private String classify = "01";
	
	

	/**
	 * 断点续答
	 */
	private boolean keep = false;

	/**
	 * 要求的答题数量
	 */
	private long anSize = 0;

	private String ipCtl = Consts.ipCtl0;
	private long ipSize = 0;

	private String cookieCtl = Consts.cookieCtl0;
	private long cookieSize = 0;

	/**
	 * 是否微信答题，true必须微信环境里答题，false可以普通浏览器里答题
	 */
	private boolean requiredWx = false;

	/**
	 * 微信答题配置
	 */
	private WxCfg wxCfg = new WxCfg();

	/**
	 * 分页IDS
	 */
	private List<PageBO> pageBOs = new ArrayList();

	/**
	 * 配额控制
	 */
	private List<QuotaGroupBO> quotaGroups = new ArrayList();

	private SurveyCfgBO cfg = new SurveyCfgBO();

	private Date created = new Date();


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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	


	/**
	 * @return the publishState
	 */
	public String getPublishState() {
		return publishState;
	}

	/**
	 * @param publishState the publishState to set
	 */
	public void setPublishState(String publishState) {
		this.publishState = publishState;
	}

	/**
	 * @return the publishTime
	 */
	public Date getPublishTime() {
		return publishTime;
	}

	/**
	 * @param publishTime the publishTime to set
	 */
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	/**
	 * @return the anSize
	 */
	public long getAnSize() {
		return anSize;
	}

	/**
	 * @param anSize
	 *            the anSize to set
	 */
	public void setAnSize(long anSize) {
		this.anSize = anSize;
	}

	/**
	 * @return the ipSize
	 */
	public long getIpSize() {
		return ipSize;
	}

	/**
	 * @param ipSize
	 *            the ipSize to set
	 */
	public void setIpSize(long ipSize) {
		this.ipSize = ipSize;
	}

	/**
	 * @return the cookieSize
	 */
	public long getCookieSize() {
		return cookieSize;
	}

	/**
	 * @param cookieSize
	 *            the cookieSize to set
	 */
	public void setCookieSize(long cookieSize) {
		this.cookieSize = cookieSize;
	}

	/**
	 * @return the pageBOs
	 */
	public List<PageBO> getPageBOs() {
		return pageBOs;
	}

	/**
	 * @param pageBOs
	 *            the pageBOs to set
	 */
	public void setPageBOs(List<PageBO> pageBOs) {
		this.pageBOs = pageBOs;
	}

	/**
	 * @return the quotaGroups
	 */
	public List<QuotaGroupBO> getQuotaGroups() {
		return quotaGroups;
	}

	/**
	 * @param quotaGroups
	 *            the quotaGroups to set
	 */
	public void setQuotaGroups(List<QuotaGroupBO> quotaGroups) {
		this.quotaGroups = quotaGroups;
	}

	/**
	 * @return the cfg
	 */
	public SurveyCfgBO getCfg() {
		return cfg;
	}

	/**
	 * @param cfg
	 *            the cfg to set
	 */
	public void setCfg(SurveyCfgBO cfg) {
		this.cfg = cfg;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the ipCtl
	 */
	public String getIpCtl() {
		return ipCtl;
	}

	/**
	 * @param ipCtl
	 *            the ipCtl to set
	 */
	public void setIpCtl(String ipCtl) {
		this.ipCtl = ipCtl;
	}

	/**
	 * @return the cookieCtl
	 */
	public String getCookieCtl() {
		return cookieCtl;
	}

	/**
	 * @param cookieCtl
	 *            the cookieCtl to set
	 */
	public void setCookieCtl(String cookieCtl) {
		this.cookieCtl = cookieCtl;
	}

	/**
	 * @return the keep
	 */
	public boolean isKeep() {
		return keep;
	}

	/**
	 * @param keep
	 *            the keep to set
	 */
	public void setKeep(boolean keep) {
		this.keep = keep;
	}

	/**
	 * @return the requiredWx
	 */
	public boolean isRequiredWx() {
		return requiredWx;
	}

	/**
	 * @param requiredWx
	 *            the requiredWx to set
	 */
	public void setRequiredWx(boolean requiredWx) {
		this.requiredWx = requiredWx;
	}

	/**
	 * @return the wxCfg
	 */
	public WxCfg getWxCfg() {
		return wxCfg;
	}

	/**
	 * @param wxCfg the wxCfg to set
	 */
	public void setWxCfg(WxCfg wxCfg) {
		this.wxCfg = wxCfg;
	}

	/**
	 * @return the pub
	 */
	public String getPub() {
		return pub;
	}

	/**
	 * @param pub the pub to set
	 */
	public void setPub(String pub) {
		this.pub = pub;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the share
	 */
	public String getShare() {
		return share;
	}

	/**
	 * @param share the share to set
	 */
	public void setShare(String share) {
		this.share = share;
	}

	/**
	 * @return the dateCtl
	 */
	public boolean isDateCtl() {
		return dateCtl;
	}

	/**
	 * @param dateCtl the dateCtl to set
	 */
	public void setDateCtl(boolean dateCtl) {
		this.dateCtl = dateCtl;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the classify
	 */
	public String getClassify() {
		return classify;
	}

	/**
	 * @param classify the classify to set
	 */
	public void setClassify(String classify) {
		this.classify = classify;
	}
	
	

}
