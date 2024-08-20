/**
 * 
 */
package com.kukababy.utils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>描述:</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2018年2月1日 上午1:06:31
 */
public class Utils {

	private static Pattern PIntReg = Pattern.compile("[1-9][0-9]{0,9}");
	
	public static boolean isPhone(String phone) {
	    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	    if (phone.length() != 11) {
	        return false;
	    } else {
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(phone);
	        return m.matches();
	    }
	}
	
	
	private static Pattern PCharDigitReg = Pattern.compile("^[a-zA-Z0-9_-]{6,20}$");
	/**
	 * 
	 * <b>描述:只能输入数字、字母、下划线、横杠6-20个字母</b>:
	 * <blockquote>
	 * <pre>
	 * </pre>
	 * </blockquote>
	 * @param userKey
	 * @return
	 */
	public static boolean isCharDigit(String userKey) {

		return PCharDigitReg.matcher(userKey).matches();
	
	}
	
	
	public static String getSHA1(String content) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 对接后的字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			return byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 *
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}


	/**
	 * 判断字符串是否为正整数<br/>
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isPInt(String val) {
		return PIntReg.matcher(val).matches();
	}

	/**
	 * 
	 * <b>描述:反射得到类的所有字段</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param obj
	 * @return
	 */
	public static Set<String> getAllFields(Object obj) {
		Class<? extends Object> cls = obj.getClass();
		Set<String> set = new HashSet();
		while (cls != null) {// 当父类为null的时候说明到达了最上层的父类(Object类).
			Field fields[] = cls.getDeclaredFields();
			for (Field field : fields) {
				set.add(field.getName());
			}
			cls = cls.getSuperclass();
		}
		return set;
	}
}
