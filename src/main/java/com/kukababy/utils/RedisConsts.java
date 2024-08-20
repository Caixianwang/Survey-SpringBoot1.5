/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kukababy.utils;

/**
 * @describe 描述
 * @author caixian_wang@sina.com
 * @date 2017-11-20
 */
public class RedisConsts {

	/**
	 * 缓层用户cookie
	 */
	public static final String cookieUserPrefix = "userCookie_";
	public static final long cookieUserTimeout = 5 * 60 * 60;// 用户登录超时1小时
	public static final long captchaTimeout = 2 * 60;// 验证码 失效时长，两分钟
	
	public static final String wxScanPrimaryPrefix = "wxScanPri_";
	
	
	/**
	 * 用户登录的cookie名字
	 */
	public static final String cookieKukaName = "KukaCookie";
	
	 public static final String captchaCookieName = "CaptchaCookie";
	 public static final String captchaPrefix = "captcha:";


	/**
	 * 问卷发布后，相关问卷的信息,不包括quess
	 */
	public static final String surveyPublishPrefix = "survey:";
	/**
	 * 问卷发布后，相关问题quess
	 */
	public static final String quessPublishPrefix = "quess:";
	
	public static final String accessTokenKey = "accessToken";
	public static final String jsapiTicketKey = "jsapiTicket";
	/**
	 * 网页授权刷新token缓层时间
	 */
	public static long pageRefreshTokenTimeout = (long)29 * 24 * 60 * 60;// 30天减1天
	/**
	 * 网页授权访问token键，和普通的accessToken不一样
	 */
	//public static final String pageAccessTokenKey = "pageAccessToken";
	
	/**
	 * 微信用户openId对应的信息
	 */
	public static final String pageOpenIdPrefix = "pageOpenId:";
	/**
	 * 缓层微信授权用户7天
	 */
	public static final long pageOpenIdTimeout = 7*24 * 60 * 60;


	
}
