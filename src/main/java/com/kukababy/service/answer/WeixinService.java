package com.kukababy.service.answer;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kukababy.entity.vo.PageAccessToken;
import com.kukababy.entity.vo.WxUser;
import com.kukababy.service.comm.BaseService;
import com.kukababy.utils.Consts;
import com.kukababy.utils.RedisConsts;
import com.kukababy.utils.Utils;

/**
 * 
 * <b>描述:</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2018年1月31日 下午2:11:21
 */

@Service
public class WeixinService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(WeixinService.class);

	/**
	 * 基础支持: 获取access_token接口 /token
	 */
	public final static String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	/**
	 * 票据
	 */
	public final static String jsapiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	public final static String pageAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	public final static String snsapiUserinfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/**
	 * 
	 * <b>描述:获取access_token接口 /token</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param appid
	 * @param secret
	 * @return
	 */
	public String getAccessToken() {
		log.info("getAccessToken_start=");
		String accessToken = redisDAO.get(RedisConsts.accessTokenKey);
		if (accessToken == null) {
			String reqUrl = accessTokenUrl.replace("APPID", Consts.appid).replace("SECRET", Consts.secret);
			// 发起GET请求获取令牌
			JSONObject accessTokenJson = restTemplate.getForEntity(reqUrl, JSONObject.class).getBody();
			log.info("getAccessToken=" + accessTokenJson.toJSONString());
			if (accessTokenJson.containsKey("access_token")) {
				accessToken = accessTokenJson.getString("access_token");
				long expiresIn = accessTokenJson.getLongValue("expires_in");
				// 缓层起来
				redisDAO.set(RedisConsts.accessTokenKey, accessToken, expiresIn);
			} else {
				log.error("accessTokenJson=" + accessTokenJson);
				throw new RuntimeException("accessTokenJson=" + accessTokenJson);
			}
		}
		return accessToken;
	}

	/**
	 * 
	 * <b>描述:通过code授权页面，获取accessToke信息</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param code
	 * @return
	 */
	public PageAccessToken getPageAccessToken(String code) {
		String reqUrl = pageAccessTokenUrl.replace("APPID", Consts.appid).replace("SECRET", Consts.secret).replace("CODE", code);
		// 发起GET请求获取令牌
		String pageAccessTokenStr = restTemplate.getForEntity(reqUrl, String.class).getBody();
		log.info("getPageAccessToken=" + pageAccessTokenStr);
		PageAccessToken pageAccessToken = JSON.parseObject(pageAccessTokenStr, PageAccessToken.class);
		if (pageAccessToken.getAccessToken() == null) {
			log.error("getPageAccessToken=" + pageAccessTokenStr);
			throw new RuntimeException("getPageAccessToken=" + pageAccessTokenStr);
		}

		return pageAccessToken;
	}

	public PageAccessToken getPageAccessTokenBak(String code) {
		// PageAccessToken pageAccessToken =
		// redisDAO.get(RedisConsts.pageAccessTokenKey, PageAccessToken.class);
		// if (pageAccessToken == null) {
		// String reqUrl = pageAccessTokenUrl.replace("APPID",
		// Consts.appid).replace("SECRET", Consts.secret).replace("CODE", code);
		// // 发起GET请求获取令牌
		// String pageAccessTokenStr = restTemplate.getForEntity(reqUrl,
		// String.class).getBody();
		// log.info("getPageAccessToken=" + pageAccessTokenStr);
		// pageAccessToken = JSON.parseObject(pageAccessTokenStr,
		// PageAccessToken.class);
		// if (pageAccessToken.getAccessToken() != null) {
		// // 缓层起来
		// redisDAO.set(RedisConsts.pageAccessTokenKey, pageAccessToken,
		// pageAccessToken.getExpiresIn());
		// } else {
		// log.error("getPageAccessToken=" + pageAccessTokenStr);
		// throw new RuntimeException("getPageAccessToken=" +
		// pageAccessTokenStr);
		// }
		// }
		return null;
	}

	/**
	 * 
	 * <b>描述:通过pageAccessToken，得到授权用户的微信详细信息</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param pageAccessToken
	 * @param openid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public WxUser getWxUserByPageAccessToken(PageAccessToken pageAccessToken) {
		WxUser wxUser = null;
		String reqUrl = snsapiUserinfoUrl.replace("ACCESS_TOKEN", pageAccessToken.getAccessToken()).replace("OPENID", pageAccessToken.getOpenid());
		// 发起GET请求获取令牌
		byte wxUserBytes[] = restTemplate.getForEntity(reqUrl, byte[].class).getBody();
		String wxUserStr = null;
		try {
			wxUserStr = new String(wxUserBytes, "utf-8");
			log.info("getWxUser=" + wxUserStr);
			wxUser = JSON.parseObject(wxUserStr, WxUser.class);
		} catch (UnsupportedEncodingException e) {
			//log.error("getWxUser=" + wxUserStr);
			throw new RuntimeException("getWxUser=" + wxUserStr);
		}
		return wxUser;
	}

	/**
	 * 
	 * <b>描述:缓层微信网页授权用户信息到redis里</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param wxUser
	 */
	public void cacheWxUser2Redis(WxUser wxUser) {
		redisDAO.set(RedisConsts.pageOpenIdPrefix + wxUser.getOpenid(), wxUser, RedisConsts.pageOpenIdTimeout);
	}

	/**
	 * 
	 * <b>描述:jsapi的票据</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param accessToken
	 * @return
	 */
	public String getJsapiTicket(String accessToken) {
		log.info("getJsapiTicket_start=" + accessToken);
		String jsapiTicket = redisDAO.get(RedisConsts.jsapiTicketKey);
		if (jsapiTicket == null) {
			String reqUrl = jsapiTicketUrl.replace("ACCESS_TOKEN", accessToken);
			// 发起GET请求获取票据
			JSONObject ticketJson = restTemplate.getForEntity(reqUrl, JSONObject.class).getBody();
			log.info("getJsapiTicket=" + ticketJson.toJSONString());
			if (ticketJson.containsKey("ticket")) {
				jsapiTicket = ticketJson.getString("ticket");
				long expiresIn = ticketJson.getLongValue("expires_in");
				// 缓层起来
				redisDAO.set(RedisConsts.jsapiTicketKey, jsapiTicket, expiresIn);
			} else {
				log.error("ticketJson=" + ticketJson);
				throw new RuntimeException("ticketJson=" + ticketJson);
			}
		}
		return jsapiTicket;
	}

	/**
	 * 
	 * <b>描述:获取签名</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param jsapiTicket
	 * @param noncestr
	 * @param timestamp
	 * @param url
	 * @return
	 */
	public String getSignature(String jsapi_ticket, String noncestr, String timestamp, String url) {
		String content = "jsapi_ticket=" + jsapi_ticket;
		content += "&noncestr=" + noncestr;
		content += "&timestamp=" + timestamp;
		content += "&url=" + url;
		log.info("content=" + content);
		String signature = Utils.getSHA1(content);
		log.info("getSignature=" + signature);
		return signature;
	}
}
