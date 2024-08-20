/**
 * 
 */
package com.kukababy.entity.mongo.bo;

/**
 * <b>描述:题型的基本属性</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年8月7日 下午2:52:23
 */

public class BaseQuesBO {
	public static final String endType = "End";// 结束题型
	public static final String startType = "Start";// 开始题型
	
	protected String _id;
	protected String quesId;
	protected String surveyId;
	protected String quesType;
	protected String htmlTitle;
	protected boolean descFlag;// 描述控制显示
	protected String htmlDesc;// 问题描述，一般在标题下面
	protected boolean tipsFlag;
	protected String htmlTips;
	protected String jumpEl;
	protected String jumpPage;// 跳转到哪页
	protected boolean durationFlag;// 答题时长秒
	protected int duration;
	protected boolean required = true;// 必答题
	protected boolean requiredShow = true;// 是否显示必答题的星号，用户可以在编辑器里加自己的提示
	protected String style = "";
	protected String pcStyle = "";
	protected String phoneStyle = "";
	protected String paddingLeft = "";
	protected String pcPaddingLeft = "";
	protected String phonePaddingLeft = "";
	protected String visibleEl;
	
	
	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
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
	 * @return the quesType
	 */
	public String getQuesType() {
		return quesType;
	}
	/**
	 * @param quesType the quesType to set
	 */
	public void setQuesType(String quesType) {
		this.quesType = quesType;
	}
	/**
	 * @return the htmlTitle
	 */
	public String getHtmlTitle() {
		return htmlTitle;
	}
	/**
	 * @param htmlTitle the htmlTitle to set
	 */
	public void setHtmlTitle(String htmlTitle) {
		this.htmlTitle = htmlTitle;
	}
	/**
	 * @return the descFlag
	 */
	public boolean isDescFlag() {
		return descFlag;
	}
	/**
	 * @param descFlag the descFlag to set
	 */
	public void setDescFlag(boolean descFlag) {
		this.descFlag = descFlag;
	}
	/**
	 * @return the htmlDesc
	 */
	public String getHtmlDesc() {
		return htmlDesc;
	}
	/**
	 * @param htmlDesc the htmlDesc to set
	 */
	public void setHtmlDesc(String htmlDesc) {
		this.htmlDesc = htmlDesc;
	}
	/**
	 * @return the tipsFlag
	 */
	public boolean isTipsFlag() {
		return tipsFlag;
	}
	/**
	 * @param tipsFlag the tipsFlag to set
	 */
	public void setTipsFlag(boolean tipsFlag) {
		this.tipsFlag = tipsFlag;
	}
	/**
	 * @return the htmlTips
	 */
	public String getHtmlTips() {
		return htmlTips;
	}
	/**
	 * @param htmlTips the htmlTips to set
	 */
	public void setHtmlTips(String htmlTips) {
		this.htmlTips = htmlTips;
	}
	/**
	 * @return the jumpEl
	 */
	public String getJumpEl() {
		return jumpEl;
	}
	/**
	 * @param jumpEl the jumpEl to set
	 */
	public void setJumpEl(String jumpEl) {
		this.jumpEl = jumpEl;
	}
	/**
	 * @return the jumpPage
	 */
	public String getJumpPage() {
		return jumpPage;
	}
	/**
	 * @param jumpPage the jumpPage to set
	 */
	public void setJumpPage(String jumpPage) {
		this.jumpPage = jumpPage;
	}
	/**
	 * @return the durationFlag
	 */
	public boolean isDurationFlag() {
		return durationFlag;
	}
	/**
	 * @param durationFlag the durationFlag to set
	 */
	public void setDurationFlag(boolean durationFlag) {
		this.durationFlag = durationFlag;
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
	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	/**
	 * @return the requiredShow
	 */
	public boolean isRequiredShow() {
		return requiredShow;
	}
	/**
	 * @param requiredShow the requiredShow to set
	 */
	public void setRequiredShow(boolean requiredShow) {
		this.requiredShow = requiredShow;
	}
	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}
	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	/**
	 * @return the pcStyle
	 */
	public String getPcStyle() {
		return pcStyle;
	}
	/**
	 * @param pcStyle the pcStyle to set
	 */
	public void setPcStyle(String pcStyle) {
		this.pcStyle = pcStyle;
	}
	/**
	 * @return the phoneStyle
	 */
	public String getPhoneStyle() {
		return phoneStyle;
	}
	/**
	 * @param phoneStyle the phoneStyle to set
	 */
	public void setPhoneStyle(String phoneStyle) {
		this.phoneStyle = phoneStyle;
	}
	/**
	 * @return the paddingLeft
	 */
	public String getPaddingLeft() {
		return paddingLeft;
	}
	/**
	 * @param paddingLeft the paddingLeft to set
	 */
	public void setPaddingLeft(String paddingLeft) {
		this.paddingLeft = paddingLeft;
	}
	/**
	 * @return the pcPaddingLeft
	 */
	public String getPcPaddingLeft() {
		return pcPaddingLeft;
	}
	/**
	 * @param pcPaddingLeft the pcPaddingLeft to set
	 */
	public void setPcPaddingLeft(String pcPaddingLeft) {
		this.pcPaddingLeft = pcPaddingLeft;
	}
	/**
	 * @return the phonePaddingLeft
	 */
	public String getPhonePaddingLeft() {
		return phonePaddingLeft;
	}
	/**
	 * @param phonePaddingLeft the phonePaddingLeft to set
	 */
	public void setPhonePaddingLeft(String phonePaddingLeft) {
		this.phonePaddingLeft = phonePaddingLeft;
	}
	
	/**
	 * @return the visibleEl
	 */
	public String getVisibleEl() {
		return visibleEl;
	}
	/**
	 * @param visibleEl the visibleEl to set
	 */
	public void setVisibleEl(String visibleEl) {
		this.visibleEl = visibleEl;
	}
	/**
	 * @return the quesId
	 */
	public String getQuesId() {
		return quesId;
	}
	/**
	 * @param quesId the quesId to set
	 */
	public void setQuesId(String quesId) {
		this.quesId = quesId;
	}
	
	

}
