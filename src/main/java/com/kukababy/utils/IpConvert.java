/**
 * 
 */
package com.kukababy.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kukababy.utils.PropUtil;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Subdivision;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年12月23日 上午11:15:11
 */
public class IpConvert {

	private static final Logger log = LoggerFactory.getLogger(IpConvert.class);
	private static final String iplib = PropUtil.get("ip.lib");
	private DatabaseReader reader = null;
	private static final IpConvert instance = new IpConvert();

	/**
	 * 获取单例对象
	 *
	 * @return
	 */
	public static IpConvert getInstance() {
		return instance;
	}

	private IpConvert() {
		try {
			File database = new File(iplib);
			// 读取数据库内容
			reader = new DatabaseReader.Builder(database).build();
		} catch (IOException ex) {
			log.error("加载IP库报错");
		}
	}

	public Map<String, String> ip2City(String ip) {
		Map<String, String> map = new HashMap();
		try {
			InetAddress ipAddress = InetAddress.getByName(ip);
			// 获取查询结果
			CityResponse response = reader.city(ipAddress);
			// 获取省份
			Subdivision subdivision = response.getMostSpecificSubdivision();
			map.put("province", subdivision.getNames().get("zh-CN"));
			// 获取城市
			City city = response.getCity();
			map.put("city", city.getNames().get("zh-CN"));
		} catch (IOException | GeoIp2Exception ex) {
			//log.error("转换IP错误");
			return null;
		}
		return map;
	}
	
}
