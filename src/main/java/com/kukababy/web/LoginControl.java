package com.kukababy.web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.kukababy.common.KaptchaCode;
import com.kukababy.entity.mongo.UserMO;
import com.kukababy.entity.vo.PageAccessToken;
import com.kukababy.entity.vo.UserVO;
import com.kukababy.entity.vo.WxUser;
import com.kukababy.key.KeyUtils;
import com.kukababy.res.ResInfo;
import com.kukababy.service.LoginService;
import com.kukababy.service.answer.WeixinService;
import com.kukababy.utils.MD5;
import com.kukababy.utils.PropUtil;
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
public class LoginControl {

	private static final Logger log = LoggerFactory.getLogger(LoginControl.class);

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/register")
	public ResInfo register(HttpServletRequest request, HttpServletResponse response, @RequestBody UserVO userVO) {
		log.info("userVO=" + JSON.toJSONString(userVO));
		ResInfo resInfo = new ResInfo();
		
		if (StringUtils.isBlank(userVO.getUserName()) || StringUtils.isBlank(userVO.getPasswd())) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("请输入登录账户和密码！");
			return resInfo;
		}
		boolean exists = false;
		exists = loginService.existsByUserName(userVO.getUserName());
		if (exists) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("用户已存在！");
			return resInfo;
		}
		
		if (!Utils.isCharDigit(userVO.getUserName())) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("6-20位的数字字母组成！");
			return resInfo;
		}
		loginService.registerUserName(userVO);
		String cookieVal = MD5.convert(userVO.getUserId() + new Date().getTime());
		Cookie ck = new Cookie(RedisConsts.cookieKukaName, cookieVal);
		// cookie.setMaxAge(2 * 60 * 60);// 设置为2小时
		ck.setPath("/");
		response.addCookie(ck);
		userVO.setKey(cookieVal);
		loginService.setUser2Redis(userVO);// 保存登录信息到缓层
		return resInfo;
	}

	/**
	 * 
	 * <b>描述:输入用户名或手机号登录</b>:
	 * <blockquote>
	 * <pre>
	 * </pre>
	 * </blockquote>
	 * @param request
	 * @param response
	 * @param userVO
	 * @return
	 */
	@RequestMapping(value = "/login")
	public ResInfo login(HttpServletRequest request, HttpServletResponse response, @RequestBody UserVO userVO) {
		log.info("userVO=" + JSON.toJSONString(userVO));
		ResInfo resInfo = new ResInfo();

		boolean success = true;
		//success = captchaValid(request, userVO, resInfo);
		//if (!success) {
			//return resInfo;
		//}

		if (StringUtils.isBlank(userVO.getUserName()) || StringUtils.isBlank(userVO.getPasswd())) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("请输入登录账户和密码！");
			return resInfo;
		}
		UserMO userMO = loginService.userLogin(userVO);
		if (userMO!=null) {
			String cookieVal = MD5.convert(userVO.getUserId() + new Date().getTime());
			Cookie ck = new Cookie(RedisConsts.cookieKukaName, cookieVal);
			//ck.setMaxAge(24 * 60 * 60);// 设置为2小时
			ck.setPath("/");
			response.addCookie(ck);
			
			loginService.userMO2UserVO(userMO, userVO);
			userVO.setKey(cookieVal);
			loginService.setUser2Redis(userVO);// 保存登录信息到缓层

		} else {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("无效的登录账户或密码！");
		}
		return resInfo;
	}
	

	@RequestMapping(value = "/imgCode")
	public void imgCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		OutputStream os = response.getOutputStream();
		// Map<String, Object> map = ImgCode.getImageCode(60, 20, os);
		Map<String, Object> map = KaptchaCode.getImageCode();
		String captcha = map.get("capText").toString().toLowerCase();
		String ckKey = KeyUtils.getInstance().getUUID("co");
		Cookie ck = new Cookie(RedisConsts.captchaCookieName, ckKey);
		ck.setPath("/");
		response.addCookie(ck);
		loginService.setCaptcha(ckKey, captcha);// 缓层验证cookie
		ImageIO.write((BufferedImage) map.get("capImg"), "JPEG", os);
	}

	private boolean captchaValid(HttpServletRequest request, UserVO userVO, ResInfo resInfo) {
		if (StringUtils.isBlank(userVO.getCaptcha())) {
			resInfo.setStatus(ResInfo.FAIL);
			resInfo.setMsg("请输入验证码！");
			return false;
		}

		Cookie[] cks = request.getCookies();
		if (cks != null) {
			for (Cookie ck : cks) {
				if (ck.getName().equals(RedisConsts.captchaCookieName)) {
					String cookieKey = ck.getValue();
					String cacheCaptcha = loginService.getCaptcha(cookieKey);
					if (StringUtils.isBlank(cacheCaptcha)) {
						resInfo.setStatus(ResInfo.FAIL);
						resInfo.setMsg("验证码已过期！");
						return false;
					} else if (userVO.getCaptcha().equals(cacheCaptcha)) {
						return true;
					} else {
						resInfo.setStatus(ResInfo.FAIL);
						resInfo.setMsg("验证码错误！");
						return false;
					}
				}
			}
		}
		resInfo.setStatus(ResInfo.FAIL);
		resInfo.setMsg("对应cookie信息没有随浏览器提交");
		return false;
	}

	@RequestMapping(value = "/logout")
	public ResInfo logout(HttpServletRequest request, HttpServletResponse response) {
		ResInfo resInfo = new ResInfo();
		log.info("logout");
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(RedisConsts.cookieKukaName)) {
					String key = cookie.getValue();
					loginService.delUserCookieOfRedis(key);
					break;
				}
			}
		}
		return resInfo;
	}

}
