package com.kukababy.web.base;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.kukababy.entity.vo.DatiDTO;
import com.kukababy.entity.vo.WxUser;
import com.kukababy.key.KeyUtils;
import com.kukababy.res.ResInfo;
import com.kukababy.utils.Consts;
import com.kukababy.utils.EscapeUnescape;
import com.kukababy.utils.IpConvert;
import com.kukababy.utils.QRCodeUtil;
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

public class BaseDesignControl extends BaseControl {

	private static final Logger log = LoggerFactory.getLogger(BaseDesignControl.class);

}
