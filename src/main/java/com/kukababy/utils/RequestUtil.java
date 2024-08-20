/**
 * 
 */
package com.kukababy.utils;

/**
 * <b>描述:</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2018年5月16日 上午10:41:25
 */
public class RequestUtil {

	/**
	 * 
	 * <b>描述:是否微信浏览器</b>:
	 * <blockquote>
	 * <pre>
	 * </pre>
	 * </blockquote>
	 * @param req
	 * @return
	 */
	public static boolean isWxBrowser(String userAgent) {
		return userAgent.indexOf("micromessenger") != -1;
	}

}
