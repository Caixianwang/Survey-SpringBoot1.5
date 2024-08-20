package com.kukababy.utils;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求校验工具类
 *
 */
public class SignUtil {

	private static final Logger log = LoggerFactory.getLogger(SignUtil.class);

	// 与开发模式接口配置信息中的Token保持一致
	// private static final String token = "ezchartToken";

	/**
	 * 校验签名
	 *
	 * @param signature
	 *            微信加密签名
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机数
	 * @return
	 */
	public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
		// 对token、timestamp和nonce按字典排序
		String[] paramArr = new String[] { token, timestamp, nonce };
		Arrays.sort(paramArr);

		// 将排序后的结果拼接成一个字符串
		String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);

		String ciphertext = Utils.getSHA1(content);

		// 将sha1加密后的字符串与signature进行对比
		return ciphertext != null ? ciphertext.equals(signature.toUpperCase()) : false;
	}

}
