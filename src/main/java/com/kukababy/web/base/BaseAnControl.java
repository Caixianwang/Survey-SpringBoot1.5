package com.kukababy.web.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.kukababy.dao.redis.RedisDAO;
import com.kukababy.entity.vo.DatiDTO;
import com.kukababy.entity.vo.WxUser;
import com.kukababy.exception.DatiException;
import com.kukababy.key.KeyUtils;
import com.kukababy.res.ResInfo;
import com.kukababy.utils.Consts;
import com.kukababy.utils.EscapeUnescape;
import com.kukababy.utils.IpConvert;
import com.kukababy.utils.RedisConsts;
import com.kukababy.utils.RequestUtil;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年4月25日 上午9:40:43
 */

public class BaseAnControl extends BaseControl {

	private static final Logger log = LoggerFactory.getLogger(BaseAnControl.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

	@Autowired
	protected RedisDAO redisDAO;

	/**
	 * 
	 * <b>描述:发送提示cookie信息到浏览器</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param res
	 * @param surveyId
	 * @param wxUser
	 */
	public static void setTipsCookie2Browser(HttpServletResponse res, Map<String, Object> tipMap) {
		String escapeTipsVO = EscapeUnescape.escape(JSON.toJSONString(tipMap));
		Cookie cookie = new Cookie(Consts.tipsCookie + tipMap.get("tipsId"), escapeTipsVO);
		cookie.setMaxAge(10 * 60);// 设置为10分钟
		cookie.setPath("/");
		// ck.setSecure(true);
		res.addCookie(cookie);
	}

	/**
	 * 
	 * <b>描述:验证是否有版本号</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param answerVO
	 * @param params
	 */
	// public static void validDatiVersion(DatiDTO datiDTO) {
	//
	// if (datiDTO.getVersion()<=0) {
	// throw new AnswerException("抱歉，没有请求到对应的问卷版本号！");
	// }
	// //answerVO.setCurrVarAnMap((Map<String, Object>) params.get("currAn"));
	// }

	/**
	 * 
	 * <b>描述:检查浏览器是否有答题cookie，并初始化answerVO的cookie</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param request
	 * @param datiDTO
	 */
	protected void validSurveyCookieFromBrowser(ResInfo resInfo,HttpServletRequest req, DatiDTO datiDTO) {
		if(resInfo.isHasError()){
			return;
		}
		initSurveyCookieFromRequest(resInfo,req, datiDTO);
		if (datiDTO.getCookie() == null) {
			resInfo.setHasError(true).setStatus(ResInfo.FAIL).setMsg("抱歉，答题需要浏览器的cookie！");
			return;
		}
	}

	/**
	 * 
	 * <b>描述:设置问卷的cookie到浏览器</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param res
	 * @param surveyId
	 */
	protected void setSurveyCookie2Browser(ResInfo resInfo,HttpServletResponse res, String surveyId) {
		if(resInfo.isHasError()){
			return;
		}
		Cookie cookie = new Cookie(Consts.surveyCookie + surveyId, KeyUtils.getInstance().getUUID("ck"));
		cookie.setMaxAge(90 * 24 * 60 * 60);// 设置为3个月
		cookie.setPath("/");
		// ck.setSecure(true);
		res.addCookie(cookie);
	}

	/**
	 * 
	 * <b>描述:1、返回问卷cookie的剩余时间，0也表示没有问卷cookie</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param surveyCookie
	 * @return
	 */
	@Deprecated
	private long getSurveyCookieOffTime(String surveyCookie) {
		long offTime = 0;
		if (surveyCookie == null) {
			return 0;
		}
		// cookie 按照1ck18052408485300000 格式
		String dar[] = surveyCookie.split("ck");
		if (dar.length == 2 && dar[1].length() > 12) {
			long cookieMaxTime = Long.parseLong(dar[1].substring(0, 12)) + 90 * 24 * 60 * 60;
			long currTime = Long.parseLong(sdf.format(new Date()));
			return cookieMaxTime - currTime;
		}
		return offTime;

	}

	/**
	 * 
	 * <b>描述:初始化ip和区域信息</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param request
	 * @param answerVO
	 */
	protected void initIpAndCity(ResInfo resInfo,HttpServletRequest req, DatiDTO datiDTO) {
		if(resInfo.isHasError()){
			return;
		}
		String ip = req.getRemoteAddr();
		datiDTO.setIp(ip);
		Map<String, String> map = IpConvert.getInstance().ip2City(ip);
		if (map != null) {
			datiDTO.setProvince(map.get("province"));
			datiDTO.setCity(map.get("city"));
		}
	}

	/**
	 * 
	 * <b>描述:浏览器请求是否有问卷cookie</b>: <blockquote>
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param req
	 * @param surveyId
	 * @return
	 */
	protected void initSurveyCookieFromRequest(ResInfo resInfo,HttpServletRequest req, DatiDTO datiDTO) {
		if(resInfo.isHasError()){
			return;
		}
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Consts.surveyCookie + datiDTO.getSurveyId())) {
					datiDTO.setCookie(cookie.getValue());// 有cookie了
				}
			}
		}
	}

	/**
	 * 
	 * <b>描述:开始请求参数装换成answerVO对象</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param answerVO
	 * @param params
	 */
	protected void readyAnRequestParams(DatiDTO datiDTO, Map<String, Object> params) {
		datiDTO.setSurveyId((String) params.get("surveyId"));
		datiDTO.setAnId((String) params.get("anId"));
		
		if (params.get("back") != null) {
			datiDTO.setBack((boolean) params.get("back"));
		}
		if (params.get("version") != null) {
			datiDTO.setVersion(Long.parseLong("" + params.get("version")));
		}
		if (params.get("wxSkip") != null) {
			datiDTO.setWxSkip((boolean) params.get("wxSkip"));
		}
		if (params.get("rtn") != null) {
			datiDTO.setRtn((boolean) params.get("rtn"));
		}
		datiDTO.setCurrVarAnMap((Map<String, Object>) params.get("currAn"));
		datiDTO.setVarAnMap((Map<String, Object>) params.get("an"));
	}

	/**
	 * 
	 * <b>描述:初始化浏览器代理信息</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param req
	 * @param answerVO
	 */
	protected void initUserAgent(HttpServletRequest req, DatiDTO datiDTO) {
		datiDTO.setUserAgent(req.getHeader("User-Agent").toLowerCase());
	}

	/**
	 * 
	 * <b>描述:初始化答题渠道</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param req
	 * @param datiDTO
	 */
	protected void initAnChannel(ResInfo resInfo,DatiDTO datiDTO) {
		if(resInfo.isHasError()){
			return ;
		}
		boolean isWxBrowser = RequestUtil.isWxBrowser(datiDTO.getUserAgent());
		if (isWxBrowser) {
			datiDTO.setAnChannel(Consts.anChannel0);
		} else {
			datiDTO.setAnChannel(Consts.anChannel1);
		}
	}

	protected void setWxPageAuthCookie(HttpServletResponse res, String surveyId, String openId) {
		// String escapeWxUser =
		// EscapeUnescape.escape(JSON.toJSONString(wxUser));
		Cookie cookie = new Cookie(Consts.wxPageAuthCookie + surveyId, openId);
		Long redisPageOpenIdTimeout =RedisConsts.pageOpenIdTimeout ;
		cookie.setMaxAge(redisPageOpenIdTimeout.intValue()-60*60);// 页面cookie少于redis的缓层时间
		cookie.setPath("/");
		// ck.setSecure(true);
		res.addCookie(cookie);
	}

	/**
	 * 
	 * <b>描述:设置微信授权的信息</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param resInfo
	 * @param req
	 * @param answerVO
	 */
	protected void initPageWxAuthFromCookie(ResInfo resInfo,HttpServletRequest req, DatiDTO datiDTO) {
		if(resInfo.isHasError()){
			return;
		}
		//if(datiDTO.isWxSkip()){//用户跳过，不用收集用户信息
			//return;
		//}
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Consts.wxPageAuthCookie + datiDTO.getSurveyId())) {
					String openId = cookie.getValue();
					WxUser wxUser = redisDAO.get(RedisConsts.pageOpenIdPrefix + openId, WxUser.class);
					datiDTO.setWxUser(wxUser);
				}
			}
		}
	}

}
