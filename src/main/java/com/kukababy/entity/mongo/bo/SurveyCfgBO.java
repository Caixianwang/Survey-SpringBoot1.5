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
public class SurveyCfgBO {
	private boolean back = false;// 是否支持回退操作，上一页、下一页按钮显示
	private String backBgColor = "";// 上一页的按钮背景色
	private String backWidth = "";//// 上页的按钮宽度
	private String backTextAlign = "right";// 上页的位置
	private String backContent = "上一页";
	private String submitBgColor = "";
	private String submitWidth = "";
	private String submitContent = "提交";
	private String submitTextAlign = "center";// 提交按钮的位置
	private String surveyBackgroundColor = "#F5F5F5";
	private String quesBackgroundColor = "#FFFFFF";

	private String surveyPcBackgroundColor = "#F5F5F5";
	private String quesPcBackgroundColor = "#FFFFFF";

	private String surveyPhoneBackgroundColor = "#DADDE1";
	private String quesPhoneBackgroundColor = "#FFFFFF";
	
	
	/**
	 * @return the back
	 */
	public boolean isBack() {
		return back;
	}
	/**
	 * @param back the back to set
	 */
	public void setBack(boolean back) {
		this.back = back;
	}
	/**
	 * @return the backBgColor
	 */
	public String getBackBgColor() {
		return backBgColor;
	}
	/**
	 * @param backBgColor the backBgColor to set
	 */
	public void setBackBgColor(String backBgColor) {
		this.backBgColor = backBgColor;
	}
	/**
	 * @return the backWidth
	 */
	public String getBackWidth() {
		return backWidth;
	}
	/**
	 * @param backWidth the backWidth to set
	 */
	public void setBackWidth(String backWidth) {
		this.backWidth = backWidth;
	}
	/**
	 * @return the backTextAlign
	 */
	public String getBackTextAlign() {
		return backTextAlign;
	}
	/**
	 * @param backTextAlign the backTextAlign to set
	 */
	public void setBackTextAlign(String backTextAlign) {
		this.backTextAlign = backTextAlign;
	}
	/**
	 * @return the backContent
	 */
	public String getBackContent() {
		return backContent;
	}
	/**
	 * @param backContent the backContent to set
	 */
	public void setBackContent(String backContent) {
		this.backContent = backContent;
	}
	/**
	 * @return the submitBgColor
	 */
	public String getSubmitBgColor() {
		return submitBgColor;
	}
	/**
	 * @param submitBgColor the submitBgColor to set
	 */
	public void setSubmitBgColor(String submitBgColor) {
		this.submitBgColor = submitBgColor;
	}
	/**
	 * @return the submitWidth
	 */
	public String getSubmitWidth() {
		return submitWidth;
	}
	/**
	 * @param submitWidth the submitWidth to set
	 */
	public void setSubmitWidth(String submitWidth) {
		this.submitWidth = submitWidth;
	}
	/**
	 * @return the submitContent
	 */
	public String getSubmitContent() {
		return submitContent;
	}
	/**
	 * @param submitContent the submitContent to set
	 */
	public void setSubmitContent(String submitContent) {
		this.submitContent = submitContent;
	}
	/**
	 * @return the submitTextAlign
	 */
	public String getSubmitTextAlign() {
		return submitTextAlign;
	}
	/**
	 * @param submitTextAlign the submitTextAlign to set
	 */
	public void setSubmitTextAlign(String submitTextAlign) {
		this.submitTextAlign = submitTextAlign;
	}
	/**
	 * @return the surveyBackgroundColor
	 */
	public String getSurveyBackgroundColor() {
		return surveyBackgroundColor;
	}
	/**
	 * @param surveyBackgroundColor the surveyBackgroundColor to set
	 */
	public void setSurveyBackgroundColor(String surveyBackgroundColor) {
		this.surveyBackgroundColor = surveyBackgroundColor;
	}
	/**
	 * @return the quesBackgroundColor
	 */
	public String getQuesBackgroundColor() {
		return quesBackgroundColor;
	}
	/**
	 * @param quesBackgroundColor the quesBackgroundColor to set
	 */
	public void setQuesBackgroundColor(String quesBackgroundColor) {
		this.quesBackgroundColor = quesBackgroundColor;
	}
	/**
	 * @return the surveyPcBackgroundColor
	 */
	public String getSurveyPcBackgroundColor() {
		return surveyPcBackgroundColor;
	}
	/**
	 * @param surveyPcBackgroundColor the surveyPcBackgroundColor to set
	 */
	public void setSurveyPcBackgroundColor(String surveyPcBackgroundColor) {
		this.surveyPcBackgroundColor = surveyPcBackgroundColor;
	}
	/**
	 * @return the quesPcBackgroundColor
	 */
	public String getQuesPcBackgroundColor() {
		return quesPcBackgroundColor;
	}
	/**
	 * @param quesPcBackgroundColor the quesPcBackgroundColor to set
	 */
	public void setQuesPcBackgroundColor(String quesPcBackgroundColor) {
		this.quesPcBackgroundColor = quesPcBackgroundColor;
	}
	/**
	 * @return the surveyPhoneBackgroundColor
	 */
	public String getSurveyPhoneBackgroundColor() {
		return surveyPhoneBackgroundColor;
	}
	/**
	 * @param surveyPhoneBackgroundColor the surveyPhoneBackgroundColor to set
	 */
	public void setSurveyPhoneBackgroundColor(String surveyPhoneBackgroundColor) {
		this.surveyPhoneBackgroundColor = surveyPhoneBackgroundColor;
	}
	/**
	 * @return the quesPhoneBackgroundColor
	 */
	public String getQuesPhoneBackgroundColor() {
		return quesPhoneBackgroundColor;
	}
	/**
	 * @param quesPhoneBackgroundColor the quesPhoneBackgroundColor to set
	 */
	public void setQuesPhoneBackgroundColor(String quesPhoneBackgroundColor) {
		this.quesPhoneBackgroundColor = quesPhoneBackgroundColor;
	}
	
	

}
