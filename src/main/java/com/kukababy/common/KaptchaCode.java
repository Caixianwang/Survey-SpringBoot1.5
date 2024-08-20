/**
 * 
 */
package com.kukababy.common;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;



/**
 * <b>描述:</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2018年3月6日 上午9:23:47
 */
public class KaptchaCode {

	public static Map<String, Object> getImageCode() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		DefaultKaptcha captchaProducer = new DefaultKaptcha();
		Properties properties = new Properties();
		
		properties.put("kaptcha.image.width", "90");
		properties.put("kaptcha.image.height", "45");
		properties.put("kaptcha.border", "no");
		properties.put("kaptcha.textproducer.font.size", "30");
		properties.put("kaptcha.textproducer.char.space", "3");
		properties.put("kaptcha.textproducer.font.color", "blue");
		properties.put("kaptcha.textproducer.char.length", "4");
		//properties.put("kaptcha.textproducer.font.names", "楷体,微软雅黑");
		properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
		properties.put("kaptcha.textproducer.char.string", "0123456789");
		Config cfg = new Config(properties);
		captchaProducer.setConfig(cfg);
		String capText = captchaProducer.createText();
		BufferedImage image = captchaProducer.createImage(capText);
		returnMap.put("capImg", image);
		returnMap.put("capText", capText);
		return returnMap;
	}

}
