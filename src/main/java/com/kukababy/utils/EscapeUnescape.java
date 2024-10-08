/**
 * 
 */
package com.kukababy.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * <b>描述:</b>:
 * <blockquote>
 * <pre>
 * </pre>
 * </blockquote>
 * @author caixian_wang@sina.com
 * @date 2018年5月15日 下午11:43:04
 */
public class EscapeUnescape {
	public static String escape(String src) {  
        int i;  
        char j;  
        StringBuffer tmp = new StringBuffer();  
        tmp.ensureCapacity(src.length() * 6);  
        for (i = 0; i < src.length(); i++) {  
            j = src.charAt(i);  
            if (Character.isDigit(j) || Character.isLowerCase(j)  
                    || Character.isUpperCase(j))  
                tmp.append(j);  
            else if (j < 256) {  
                tmp.append("%");  
                if (j < 16)  
                    tmp.append("0");  
                tmp.append(Integer.toString(j, 16));  
            } else {  
                tmp.append("%u");  
                tmp.append(Integer.toString(j, 16));  
            }  
        }  
        return tmp.toString();  
    }  
  
    public static String unescape(String src) {  
        StringBuffer tmp = new StringBuffer();  
        tmp.ensureCapacity(src.length());  
        int lastPos = 0, pos = 0;  
        char ch;  
        while (lastPos < src.length()) {  
            pos = src.indexOf("%", lastPos);  
            if (pos == lastPos) {  
                if (src.charAt(pos + 1) == 'u') {  
                    ch = (char) Integer.parseInt(src  
                            .substring(pos + 2, pos + 6), 16);  
                    tmp.append(ch);  
                    lastPos = pos + 6;  
                } else {  
                    ch = (char) Integer.parseInt(src  
                            .substring(pos + 1, pos + 3), 16);  
                    tmp.append(ch);  
                    lastPos = pos + 3;  
                }  
            } else {  
                if (pos == -1) {  
                    tmp.append(src.substring(lastPos));  
                    lastPos = src.length();  
                } else {  
                    tmp.append(src.substring(lastPos, pos));  
                    lastPos = pos;  
                }  
            }  
        }  
        return tmp.toString();  
    }  
  
    /**   
     * @disc 对字符串重新编码   
     * @param src   
     * @return    
     */  
    public static String isoToGB(String src) {  
        String strRet = null;  
        try {  
            strRet = new String(src.getBytes("ISO_8859_1"), "GB2312");  
        } catch (Exception e) {  
  
        }  
        return strRet;  
    }  
  
    /**   
     * @disc 对字符串重新编码   
     * @param src   
     * @return    
     */  
    public static String isoToUTF(String src) {  
        String strRet = null;  
        try {  
            strRet = new String(src.getBytes("ISO_8859_1"), "UTF-8");  
        } catch (Exception e) {  
  
        }  
        return strRet;  
    }  
  
    public static void main(String[] args) {  
    	Map map = new HashMap();
		map.put("city", " 列  , ;");
		map.put("nickname", "王,测试  好;");
		//String str = JSON.toJSONString(map);
        String tmp = JSON.toJSONString(map);  
        System.out.println("testing escape : " + tmp);  
        tmp = escape(tmp);  
        System.out.println(tmp);  
        System.out.println("testing unescape :" + tmp);  
        System.out.println(unescape(tmp));  
       // System.out.println(isoToUTF(tmp));  
    }  
}
