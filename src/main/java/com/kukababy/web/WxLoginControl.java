package com.kukababy.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.WriterException;
import com.kukababy.common.ThreadCache;
import com.kukababy.entity.mongo.UserMO;
import com.kukababy.entity.vo.PageAccessToken;
import com.kukababy.entity.vo.UserVO;
import com.kukababy.entity.vo.WxUser;
import com.kukababy.key.KeyUtils;
import com.kukababy.res.ResInfo;
import com.kukababy.service.LoginService;
import com.kukababy.service.WxLoginService;
import com.kukababy.service.answer.WeixinService;
import com.kukababy.utils.MD5;
import com.kukababy.utils.PropUtil;
import com.kukababy.utils.QRCodeUtil;
import com.kukababy.utils.RedisConsts;
import com.kukababy.utils.Utils;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年12月5日 上午9:40:43
 */
@RestController
public class WxLoginControl {

	private static final Logger log = LoggerFactory.getLogger(WxLoginControl.class);

	@Autowired
	private LoginService loginService;

	@Autowired
	private WxLoginService wxLoginService;

	@Autowired
	private WeixinService weixinService;

	@RequestMapping(value = "/design/modifyAccount")
	public ResInfo modifyAccount(@RequestParam String userName) {
		ResInfo resInfo = new ResInfo();
		if (!Utils.isCharDigit(userName)) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("6-20位的数字字母组成！");
			return resInfo;
		}
		boolean exists = loginService.existsByUserName(userName);
		if (exists) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("此用户名已被使用！");
			return resInfo;
		}
		UserVO user = ThreadCache.getUser();
		this.loginService.modifyUserName(user.getUserId(), userName);
		return resInfo;
	}

	@RequestMapping(value = "/design/cancelBingWx")
	public ResInfo cancelBingWx() {
		ResInfo res = new ResInfo();
		UserVO userVO = ThreadCache.getUser();
		wxLoginService.cancelBindWx(userVO.getUserId());
		userVO.setWxUse(false);
		loginService.setUser2Redis(userVO);
		ThreadCache.setUser(userVO);
		return res;
	}

	@RequestMapping(value = "/getPriId")
	public ResInfo getPrimaryId() {
		ResInfo res = new ResInfo();
		String primaryId = KeyUtils.getInstance().getUUID("p");
		res.setRes(primaryId);
		return res;
	}

	/**
	 * 
	 * <b>描述:微信扫描登录时，循环验证扫描是否成功</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param priId
	 * @return
	 */
	@RequestMapping(value = "/wxScanState")
	public ResInfo wxScanState(HttpServletResponse response, @RequestParam String priId) {
		ResInfo res = new ResInfo();
		res.setRes(false);
		UserVO userVO = loginService.getWxScanUserOfRedis(priId);
		if (userVO != null) {
			String cookieVal = MD5.convert(userVO.getUserId() + new Date().getTime());
			Cookie ck = new Cookie(RedisConsts.cookieKukaName, cookieVal);
			// ck.setMaxAge(24 * 60 * 60);// 设置为6小时
			ck.setPath("/");
			response.addCookie(ck);
			userVO.setKey(cookieVal);
			loginService.delWxScanUserOfRedis(priId);
			loginService.setUser2Redis(userVO);// 保存登录信息到缓层
			res.setRes(true);
		}
		return res;
	}

	@RequestMapping(value = "/getScanBindQrcode", method = RequestMethod.GET)
	public void getScanBindQrcode(HttpServletRequest request, HttpServletResponse response, @RequestParam String key)
			throws MalformedURLException, WriterException, IOException {
		OutputStream os = response.getOutputStream();

		String basePath = request.getSession().getServletContext().getRealPath("");
		String logoUrl = basePath + "resource/img/logo.png";
		String wxCallDomain = PropUtil.get("wx.call.domain");
		String url = wxCallDomain + "/wxScanBindRedirect?key=" + key;

		BufferedImage bufImage = QRCodeUtil.createImage(url, logoUrl, 256, true);
		log.info(key);
		ImageIO.write(bufImage, "PNG", os);

	}

	@RequestMapping(value = "/wxScanBindRedirect", method = RequestMethod.GET)
	public void wxScanBindRedirect(HttpServletResponse response, @RequestParam String key) throws IOException {
		String redirectUrl = this.wxPageAuth("/wxScanBindByCode", key);
		// log.info(redirectUrl);
		response.sendRedirect(redirectUrl);
	}

	/**
	 * 
	 * <b>描述:绑定微信</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param response
	 * @param code
	 * @param state
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/wxScanBindByCode", method = RequestMethod.GET)
	public ModelAndView wxScanBindByCode(HttpServletResponse response, @RequestParam String code, @RequestParam String state) throws IOException {
		ResInfo resInfo = new ResInfo();
		//log.info("code=" + code + " state=" + state);
		String key = state ;
		// 通过code获取pageAccessToken
		PageAccessToken pageAccessToken = weixinService.getPageAccessToken(code);
		WxUser wxUser = weixinService.getWxUserByPageAccessToken(pageAccessToken);

		UserMO userMO = wxLoginService.getUserByOpenidAndUse(pageAccessToken.getOpenid());
		if (userMO != null) {
			UserVO userVO = new UserVO();
			userVO.setOther("Y");
			loginService.setWxScanUser2Redis(key, userVO);// 页面不停刷新，检查用户是否已扫描微信登录
			Map<String, Object> model = new HashMap();
			return new ModelAndView("scanError", model);
		} else {
			UserVO userVO = loginService.getUserOfRedis(key);
			wxLoginService.bindUserByOpenid(userVO.getUserId(), pageAccessToken.getOpenid(), wxUser.getNickname());
			wxLoginService.otherWxUserDel(userVO.getUserId(), pageAccessToken.getOpenid());
			userVO.setWxUse(true);
			userVO.setWxNickname(wxUser.getNickname());
			loginService.setUser2Redis(userVO);
			loginService.setWxScanUser2Redis(state, userVO);// 页面不停刷新，检查用户是否已扫描微信登录
			Map<String, Object> model = new HashMap();
			return new ModelAndView("scanSuccess", model);
		}

	}

	@RequestMapping(value = "/wxScanBindState")
	public ResInfo wxScanBindState(HttpServletResponse response, @RequestParam String key) {
		ResInfo res = new ResInfo();
		UserVO userVO = loginService.getWxScanUserOfRedis(key);
		if (userVO != null) {
			if(userVO.getOther()!=null&&userVO.getOther().equals("Y")){	
				loginService.delWxScanUserOfRedis(key);
				return res.setRes(new UserVO());
			}else{
				res.setRes(userVO);
			}
			loginService.delWxScanUserOfRedis(key);
		}
		return res;
	}

	@RequestMapping(value = "/getScanLoginQrcode", method = RequestMethod.GET)
	public void getScanLoginQrcode(HttpServletRequest request, HttpServletResponse response, @RequestParam String priId)
			throws MalformedURLException, WriterException, IOException {
		OutputStream os = response.getOutputStream();

		String basePath = request.getSession().getServletContext().getRealPath("");
		String logoUrl = basePath + "resource/img/logo.png";
		String wxCallDomain = PropUtil.get("wx.call.domain");
		String url = wxCallDomain + "/wxScanLoginRedirect?priId=" + priId;

		BufferedImage bufImage = QRCodeUtil.createImage(url, logoUrl, 256, true);
		log.info(priId);
		ImageIO.write(bufImage, "PNG", os);

	}

	@RequestMapping(value = "/wxScanLoginRedirect", method = RequestMethod.GET)
	public void wxScanLoginRedirect(HttpServletResponse response, @RequestParam String priId) throws IOException {
		String redirectUrl = this.wxPageAuth("/wxScanLoginByCode", priId);
		// log.info(redirectUrl);
		response.sendRedirect(redirectUrl);
	}

	@RequestMapping(value = "/wxScanLoginByCode", method = RequestMethod.GET)
	public ModelAndView wxScanLoginByCode(HttpServletResponse response, @RequestParam String code, @RequestParam String state) throws IOException {
		ResInfo resInfo = new ResInfo();
		log.info("code=" + code + " state=" + state);
		// 通过code获取pageAccessToken
		UserMO userMO = accessWxHandle(code);
		UserVO userVO = new UserVO();
		loginService.userMO2UserVO(userMO, userVO);

		loginService.setWxScanUser2Redis(state, userVO);// 页面不停刷新，检查用户是否已扫描微信登录
		Map<String, Object> model = new HashMap();
		return new ModelAndView("scanSuccess", model);

	}

	/**
	 * 
	 * <b>描述:微信登录</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/wxLoginRedirect", method = RequestMethod.GET)
	public void wxLoginRedirect(HttpServletResponse response) throws IOException {

		String redirectUrl = this.wxPageAuth("/wxLoginByCode", "user");
		// log.info(redirectUrl);
		response.sendRedirect(redirectUrl);
	}

	private String wxPageAuth(String codeApi, String state) {
		String wxCallDomain = PropUtil.get("wx.call.domain");
		String authPage = URLEncoder.encode(wxCallDomain + codeApi);
		//String authPage = URLEncoder.encode(wxCallDomain + codeApi,"utf-8");
		String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf63021da44864cf1&redirect_uri=";
		redirectUrl += authPage;
		redirectUrl += "&response_type=code&scope=snsapi_userinfo&state=";
		redirectUrl += state;
		redirectUrl += "&connect_redirect=1#wechat_redirect";

		return redirectUrl;

	}
	
	public static void main(String args[]){
		String wxCallDomain = "http://www.kukababy.com/wxLoginByCode";
		String authPage = URLEncoder.encode(wxCallDomain);
		//String authPage = URLEncoder.encode(wxCallDomain + codeApi,"utf-8");
		String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd1283a81c56e94dd&redirect_uri=";
		redirectUrl += authPage;
		redirectUrl += "&response_type=code&scope=snsapi_userinfo&state=";
		redirectUrl += "user";
		redirectUrl += "&connect_redirect=1#wechat_redirect";
		log.info(redirectUrl);
	}

	private UserMO accessWxHandle(String code) {
		PageAccessToken pageAccessToken = weixinService.getPageAccessToken(code);
		WxUser wxUser = weixinService.getWxUserByPageAccessToken(pageAccessToken);
		UserMO userMO = wxLoginService.getUserByOpenid(pageAccessToken.getOpenid());

		if (userMO != null) {
			if (!userMO.isWxUse()) {// 曾经使用，后来又取消绑定，本次自动绑定
				wxLoginService.updateWxUserAndNickName(userMO.getUserId(), wxUser.getNickname());
			}
		} else {
			userMO = wxLoginService.createUserByOpenid(pageAccessToken.getOpenid(), wxUser.getNickname());
		}
		return userMO;
	}

	@RequestMapping(value = "/wxLoginByCode", method = RequestMethod.GET)
	public void wxLoginByCode(HttpServletResponse response, @RequestParam String code, @RequestParam String state) throws IOException {
		ResInfo resInfo = new ResInfo();
		log.info("code=" + code + " state=" + state);
		// 通过code获取pageAccessToken
		UserMO userMO = accessWxHandle(code);

		UserVO userVO = new UserVO();
		loginService.userMO2UserVO(userMO, userVO);

		String cookieVal = MD5.convert(userVO.getUserId() + new Date().getTime());
		Cookie ck = new Cookie(RedisConsts.cookieKukaName, cookieVal);
		// ck.setMaxAge(24 * 60 * 60);// 设置为2小时
		ck.setPath("/");
		response.addCookie(ck);
		userVO.setKey(cookieVal);
		loginService.setUser2Redis(userVO);// 保存登录信息到缓层

		String wxCallDomain = PropUtil.get("wx.call.domain");
		//
		String redirectUrl = wxCallDomain + "/design.html#/SurveyList";
		response.sendRedirect(redirectUrl);
	}

}
