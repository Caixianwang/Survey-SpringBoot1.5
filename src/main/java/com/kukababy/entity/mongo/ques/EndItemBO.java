/**
 * 
 */
package com.kukababy.entity.mongo.ques;

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

public class EndItemBO {
	private String htmlLabel;
	private String val = "A";
	private String desc = "正常答题结束";
	private String url = "http://www.kukababy.com";
	private boolean urlShow;
	private boolean dft = true;// 默认显示本选项

	/**
	 * @return the htmlLabel
	 */
	public String getHtmlLabel() {
		return htmlLabel;
	}

	/**
	 * @param htmlLabel
	 *            the htmlLabel to set
	 */
	public void setHtmlLabel(String htmlLabel) {
		this.htmlLabel = htmlLabel;
	}

	/**
	 * @return the val
	 */
	public String getVal() {
		return val;
	}

	/**
	 * @param val
	 *            the val to set
	 */
	public void setVal(String val) {
		this.val = val;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the urlShow
	 */
	public boolean isUrlShow() {
		return urlShow;
	}

	/**
	 * @param urlShow
	 *            the urlShow to set
	 */
	public void setUrlShow(boolean urlShow) {
		this.urlShow = urlShow;
	}

	/**
	 * @return the dft
	 */
	public boolean isDft() {
		return dft;
	}

	/**
	 * @param dft
	 *            the dft to set
	 */
	public void setDft(boolean dft) {
		this.dft = dft;
	}

}
