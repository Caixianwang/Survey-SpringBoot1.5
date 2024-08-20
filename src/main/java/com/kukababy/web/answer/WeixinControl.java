/**
 * 
 */
package com.kukababy.web.answer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kukababy.entity.vo.PageAccessToken;
import com.kukababy.entity.vo.WxUser;
import com.kukababy.res.ResInfo;
import com.kukababy.service.answer.WeixinService;
import com.kukababy.utils.Consts;
import com.kukababy.utils.PropUtil;
import com.kukababy.utils.SignUtil;
import com.kukababy.web.base.BaseAnControl;

/**
 * <b>描述:</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2018年5月11日 下午6:00:50
 */
@RestController
@RequestMapping("/wx")
public class WeixinControl extends BaseAnControl {
	private static final Logger log = LoggerFactory.getLogger(WeixinControl.class);

	@Autowired
	private WeixinService weixinService;
	
	@RequestMapping(value = "/xbSignature", method = RequestMethod.GET)
	public void xbSignature(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		log.info(signature + "," + timestamp + "," + nonce + "," + echostr);

		PrintWriter out = response.getWriter();

		// 请求校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(Consts.token, signature, timestamp, nonce)) {
			log.info(echostr);
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	@RequestMapping(value = "/getAccessToken", method = RequestMethod.GET)
	public ResInfo getAccessToken() throws IOException {
		ResInfo resInfo = new ResInfo();
		String accessToken = weixinService.getAccessToken();
		Map<String, Object> resMap = new HashMap();
		resMap.put("access_token", accessToken);
		resInfo.setRes(resMap);
		return resInfo;
	}

	@RequestMapping(value = "/getJsapiTicket", method = RequestMethod.GET)
	public ResInfo getJsapiTicket(@RequestParam String access_token) throws IOException {
		ResInfo resInfo = new ResInfo();
		String jsapiTicket = weixinService.getJsapiTicket(access_token);
		Map<String, Object> resMap = new HashMap();
		resMap.put("ticket", jsapiTicket);
		resInfo.setRes(resMap);
		return resInfo;
	}

	@RequestMapping(value = "/getSignature", method = RequestMethod.GET)
	public ResInfo getSignature(@RequestParam String jsapi_ticket, @RequestParam String noncestr, @RequestParam String timestamp, @RequestParam String url) {
		ResInfo resInfo = new ResInfo();
		String signature = weixinService.getSignature(jsapi_ticket, noncestr, timestamp, url);
		Map<String, Object> resMap = new HashMap();
		resMap.put("signature", signature);
		resInfo.setRes(resMap);
		return resInfo;
	}

	/**
	 * 
	 * <b>描述:网页授权，重定向获取code</b>: <blockquote>
	 * 
	 * <pre>
	 * 1、用户接收后得到code
	 * 2、跳转到getSnsapiUser，获得用户
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/wxPageAuthRedirect", method = RequestMethod.GET)
	public void wxPageAuthRedirect(HttpServletResponse response, @RequestParam String surveyId) throws IOException {
		//微信回调的域名
		String wxCallDomain = PropUtil.get("wx.call.domain");
		String authPage = URLEncoder.encode(wxCallDomain+"/wx/wxPageAuthByCode");
		String state = surveyId;
		String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf63021da44864cf1&redirect_uri=";
		redirectUrl += authPage;
		redirectUrl += "&response_type=code&scope=snsapi_userinfo&state=";
		redirectUrl += state;
		redirectUrl += "&connect_redirect=1#wechat_redirect";

		//log.info(redirectUrl);
		response.sendRedirect(redirectUrl);
	}

	/**
	 * 
	 * <b>描述:通过票据code，获取授权页面用户登录信息</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param code
	 * @param state
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/wxPageAuthByCode", method = RequestMethod.GET)
	public void wxPageAuthByCode(HttpServletResponse res, @RequestParam String code, @RequestParam String state) throws IOException {
		ResInfo resInfo = new ResInfo();
		log.info("code=" + code + " state=" + state);
		String surveyId = state;
		//通过code获取pageAccessToken
		PageAccessToken pageAccessToken = weixinService.getPageAccessToken(code);
		WxUser wxUser = weixinService.getWxUserByPageAccessToken(pageAccessToken);
		//log.info("wxPageAuthCookie="+JSON.toJSONString(wxUser));
		//按照openid缓层用户信息到redis
		weixinService.cacheWxUser2Redis(wxUser);
		//按照openid方式，缓层到cookie里
		this.setWxPageAuthCookie(res, surveyId, wxUser.getOpenid());
		String datiDomain = PropUtil.get("dati.domain");
		//再次打开答题页面
		String redirectUrl = datiDomain+"/dati.html?sd=" + surveyId;
		
		res.sendRedirect(redirectUrl);
	}

}
