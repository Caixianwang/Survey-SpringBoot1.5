/**
 * 
 */
package com.kukababy.entity.mongo.ques;

import com.kukababy.entity.mongo.bo.BaseQuesBO;

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

public class StartQuesBO extends BaseQuesBO {
	private boolean use = true;// 是否显示
	private String bgColor = "";// 按钮北京颜色
	private String width = "";
	private String width1 = "";// 标签宽度
	private String pcWidth = "100%";
	private String pcWidth1 = "20%";
	private String phoneWidth = "100%";
	private String phoneWidth1 = "100%";
	private String btTextAlign = "center";// 按钮的位置
	private String pcBtTextAlign = "center";
	private String phoneBtTextAlign = "center";

	public StartQuesBO() {
		this.quesType = BaseQuesBO.startType;
		//this.htmlTitle = "<p><span style='color:#27ae60'>&nbsp; &nbsp; &nbsp; &nbsp;<span style='font-size:14px'> <strong>欢迎使用酷卡儿童成长中心问卷平台，在这里您将通过所见即所得的方式自由设计问卷；酷卡问卷平台站在前辈们的臂膀上，结合客户的需求，增加一些自己的想法，历经一年的时间打造而成。</strong></span></span></p>"
				//+ "<p><span style='color:#27ae60'>&nbsp; &nbsp; &nbsp;<span style='font-size:10px'>&nbsp;</span></span><span style='font-size:10px'><span style='color:#f39c12'>酷卡问卷平台主要有下面的功能：</span></span></p>"
				//+ "<p>&nbsp; &nbsp; &nbsp;<span style='font-size:10px'><span style='color:#4e5f70'>&nbsp;A、所见所得编辑问卷，快速测试问卷</span></span></p>"
				//+ "<p><span style='font-size:10px'><span style='color:#4e5f70'>&nbsp; &nbsp; &nbsp; &nbsp; B、编辑现有问卷，发布的生产和测试不受影响</span></span></p>"
				//+ "<p><span style='font-size:10px'><span style='color:#4e5f70'>&nbsp; &nbsp; &nbsp; &nbsp; C、容易定义逻辑跳转，定义问卷逻辑</span></span></p>"
				//+ "<p><span style='font-size:10px'><span style='color:#4e5f70'>&nbsp; &nbsp; &nbsp; &nbsp; D、支持微信答题；手机和PC答题配置不同的风格</span></span></p>"
				//+ "<p><span style='font-size:10px'><span style='color:#4e5f70'>&nbsp; &nbsp; &nbsp; &nbsp; D、回收数、Cookie、IP、地区、配额等质量控制</span></span></p>"
				//+ "<p>&nbsp; &nbsp; &nbsp; &nbsp;<span style='font-size:14px'><span style='font-family:隶书'>正在成长，还需要您的宝贵意见<img alt='mail' height='23' src='http://www.kukababy.com/kukaSurvey2/resource/ckeditor/plugins/smiley/images/envelope.png' title='mail' width='23' /></span></span><span style='font-size:16px'><span style='font-family:隶书'> </span></span></p>";
	}

	/**
	 * @return the use
	 */
	public boolean isUse() {
		return use;
	}

	/**
	 * @param use
	 *            the use to set
	 */
	public void setUse(boolean use) {
		this.use = use;
	}

	/**
	 * @return the bgColor
	 */
	public String getBgColor() {
		return bgColor;
	}

	/**
	 * @param bgColor
	 *            the bgColor to set
	 */
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @return the width1
	 */
	public String getWidth1() {
		return width1;
	}

	/**
	 * @param width1
	 *            the width1 to set
	 */
	public void setWidth1(String width1) {
		this.width1 = width1;
	}

	/**
	 * @return the pcWidth
	 */
	public String getPcWidth() {
		return pcWidth;
	}

	/**
	 * @param pcWidth
	 *            the pcWidth to set
	 */
	public void setPcWidth(String pcWidth) {
		this.pcWidth = pcWidth;
	}

	/**
	 * @return the pcWidth1
	 */
	public String getPcWidth1() {
		return pcWidth1;
	}

	/**
	 * @param pcWidth1
	 *            the pcWidth1 to set
	 */
	public void setPcWidth1(String pcWidth1) {
		this.pcWidth1 = pcWidth1;
	}

	/**
	 * @return the phoneWidth
	 */
	public String getPhoneWidth() {
		return phoneWidth;
	}

	/**
	 * @param phoneWidth
	 *            the phoneWidth to set
	 */
	public void setPhoneWidth(String phoneWidth) {
		this.phoneWidth = phoneWidth;
	}

	/**
	 * @return the phoneWidth1
	 */
	public String getPhoneWidth1() {
		return phoneWidth1;
	}

	/**
	 * @param phoneWidth1
	 *            the phoneWidth1 to set
	 */
	public void setPhoneWidth1(String phoneWidth1) {
		this.phoneWidth1 = phoneWidth1;
	}

	/**
	 * @return the btTextAlign
	 */
	public String getBtTextAlign() {
		return btTextAlign;
	}

	/**
	 * @param btTextAlign
	 *            the btTextAlign to set
	 */
	public void setBtTextAlign(String btTextAlign) {
		this.btTextAlign = btTextAlign;
	}

	/**
	 * @return the pcBtTextAlign
	 */
	public String getPcBtTextAlign() {
		return pcBtTextAlign;
	}

	/**
	 * @param pcBtTextAlign
	 *            the pcBtTextAlign to set
	 */
	public void setPcBtTextAlign(String pcBtTextAlign) {
		this.pcBtTextAlign = pcBtTextAlign;
	}

	/**
	 * @return the phoneBtTextAlign
	 */
	public String getPhoneBtTextAlign() {
		return phoneBtTextAlign;
	}

	/**
	 * @param phoneBtTextAlign
	 *            the phoneBtTextAlign to set
	 */
	public void setPhoneBtTextAlign(String phoneBtTextAlign) {
		this.phoneBtTextAlign = phoneBtTextAlign;
	}

}
