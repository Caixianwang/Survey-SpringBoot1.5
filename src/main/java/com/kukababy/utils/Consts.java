/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kukababy.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @describe 描述
 * @author caixian_wang@sina.com
 * @date 2014-3-26
 */
public class Consts {

	public static final String SUCCESS = "Y";//
	public static final String OUT = "O";// session过期
	public static final String FAIL = "F";// 业务操作失败
	public static final String ERROR = "E";// 系统错误
	public static final String LANGUAGE_ZH = "zh";
	public static final String LANGUAGE_EN = "en";

	public static final String UTF_8 = "utf-8";
	public static final String GBK = "gbk";

	public static final String YES = "Y";
	public static final String NO = "N";

	public static final String COMMA = ",";
	public static final String TAB = "\t";
	public static final String CR = "\r";
	public static final String LF = "\n";
	public static final String CRLF = "\r\n";

	public static final String stringSplit = "#@#";

	public static final String surveyTestSplit = "@@";
	/**
	 * 问卷答案cookie
	 */
	public static final String surveyCookie = "surveyCookie_";
	
	/**
	 * 问卷tips.html页面提示cookie
	 */
	public static final String surveyTipsCookie = "surveyTipsCookie_";

	/**
	 * 微信网页授权保存用户信息
	 */
	public static final String wxPageAuthCookie = "wxPageAuth_";
	
	public static final String tipsCookie = "tipsCookie_";

	public static final String token = "kukababyToken";
	public static final String appid = "wxf63021da44864cf1";
	public static final String secret = "31cf63c867af322b3922ce2a9ef8f388";
	
	public static final String mimePpt = "application/vnd.ms-powerpoint";
    public static final String mimePdf = "application/pdf";
    public static final String mimeExcel = "application/vnd.ms-excel";
    public static final String mimeCsv = "application/vnd.csv";
    public static final String mimeBmp = "image/bmp";
    public static final String mimeGif = "image/gif";
    public static final String mimeJpeg = "image/jpeg";
    public static final String mimePng = "image/png";
    public static final String mimeTxt = "text/plain";
    public static final Map mimeTypeSuffix = new HashMap();

    static {
    	mimeTypeSuffix.put("application/vnd.ms-powerpoint", ".ppt");
    	mimeTypeSuffix.put("application/pdf", ".pdf");
    	mimeTypeSuffix.put("application/vnd.ms-excel", ".xls");
    	mimeTypeSuffix.put("application/vnd.csv", ".csv");
    	mimeTypeSuffix.put("image/bmp", ".bmp");
    	mimeTypeSuffix.put("image/gif", ".gif");
    	mimeTypeSuffix.put("image/jpeg", ".jpg");
    	mimeTypeSuffix.put("image/png", ".png");
    }

	/**
	 * 答题渠道0：微信答题；1：直接链接
	 */
	public static final String anChannel0 = "0";
	/**
	 * 答题渠道0：微信答题；1：直接链接
	 */
	public static final String anChannel1 = "1";

	/**
	 * 问卷没有发布
	 */
	public static final String resCode01 = "01";

	
	/**
	 * 需要在微信平台答题
	 */
	public static final String resWxCode10 = "wx10";
	/**
	 * 微信答题必须登录,显示登录按钮
	 */
	public static final String resWxCode11 = "wx11";
	/**
	 * 微信答题用户登录或不登录，显示登录按钮或不登录
	 */
	public static final String resWxCode12 = "wx12";

	/**
	 * 答题cookie数量，时间控制 0,所有时间段
	 */
	public static final String cookieCtl0 = "0";
	/**
	 * 答题cookie数量，时间控制 1,一天内
	 */
	public static final String cookieCtl1 = "1";
	/**
	 * 答题cookie数量，时间控制 ,2一小时内
	 */
	public static final String cookieCtl2 = "2";

	/**
	 * 答题ip数量，时间控制 0,所有时间段
	 */
	public static final String ipCtl0 = "0";
	/**
	 * 答题ip数量，时间控制 1,一天内
	 */
	public static final String ipCtl1 = "1";
	/**
	 * 答题ip数量，时间控制 2,一小时内
	 */
	public static final String ipCtl2 = "2";

	/**
	 * 微信数量，时间控制 0,所有时间段
	 */
	public static final String wxCtl0 = "0";
	/**
	 * 微信数量，时间控制 1，一天时间段
	 */
	public static final String wxCtl1 = "1";

}
