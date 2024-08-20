/**
 * 
 */
package com.kukababy.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>描述:</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2018年1月27日 下午7:59:41
 */
public class DateUtil {
	private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static DateInfo getDateInfo(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		String year = "" + calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONDAY) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);

		String quarter = year + "." + (month / 4 + 1);// 季度

		String strMonth = year + "." + (month < 10 ? "0" + month : "" + month);
		String strWeek = year + "." + (week < 10 ? "0" + week : "" + week);
		String strDay = year + "." + (month < 10 ? "0" + month : "" + month) + "." + (day < 10 ? "0" + day : "" + day);
		return new DateInfo(year, quarter, strMonth, strWeek, strDay);
	}

	public static DateInfo getDateInfo(Date date) {
		return getDateInfo(date.getTime());
	}

	

	/**
	 * 
	 * <b>描述:今天的开始日期</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date todayStart(Date currDate) {
		return beforeDate(currDate, 0);
	}

	/**
	 * 
	 * <b>描述:今天的结束日期，即明天的凌晨000</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date todayEnd(Date currDate) {
		return beforeDate(currDate, -1);
	}
	
	/**
	 * 
	 * <b>描述:当前小时的开始日期</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date hourStart(Date currDate) {
		return beforeHour(currDate, 0,false);
	}

	/**
	 * 
	 * <b>描述:当前小时的结束日期，即下小时的开始00</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date hourEnd(Date currDate) {
		return beforeHour(currDate, -1,false);
	}
	

	/**
	 * 
	 * <b>描述:当前日期前几小时的日期</b>: <blockquote>
	 * 
	 * <pre>
	 * 1、flag 为true 标识需要返回分钟和秒，否则只返回到小时
	 * 2、0 标识当前小时的开始时间
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @param hours
	 * @param flag
	 * @return
	 */
	public static Date beforeHour(Date currDate, int hours, boolean flag) {
		Calendar calendar = Calendar.getInstance();
		long plus = (long) 60 * 60 * 1000 * hours;
		long startMillis = currDate.getTime() - plus;
		calendar.setTimeInMillis(startMillis);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONDAY);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (flag) {// 需要时分秒
			return calendar.getTime();
		} else {
			calendar.set(year, month, day, hour, 0, 0);
		}

		return calendar.getTime();
	}

	public static Date getYMD(Date currDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currDate.getTime());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONDAY);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, day, 0, 0, 0);
		return calendar.getTime();

	}
	
	/**
	 * 
	 * <b>描述:前几天的日期 </b>: <blockquote>
	 * 
	 * <pre>
	 * 1、年月日  其中 时分秒（为0）
	 * 2、具体某天的开始时间 ，从零点开始
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param endDate
	 * @param days
	 * @return
	 */
	public static Date beforeDate(Date endDate, int days) {
		return beforeDate(endDate, days, false);
	}

	public static Date beforeDate(Date endDate, int days, boolean flag) {
		Calendar calendar = Calendar.getInstance();
		long plus = (long) 24 * 60 * 60 * 1000 * days;
		long startMillis = endDate.getTime() - plus;
		calendar.setTimeInMillis(startMillis);

		if (flag) {// 需要时分秒
			return calendar.getTime();
		} else {
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONDAY);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			calendar.set(year, month, day, 0, 0, 0);
		}
		return calendar.getTime();
	}

	/**
	 * 
	 * <b>描述:本周的第一天的时间，也是上周的最后一天</b>: <blockquote>
	 * 
	 * <pre>
	 * 1、周日为一周第一天
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date firstDayOfWeek(Date currDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currDate);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return beforeDate(currDate, dayOfWeek - 1);
	}

	/**
	 * 
	 * <b>描述:上周的第一天的时间</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date firstDayOfLastWeek(Date currDate) {
		Date firstDayOfWeek = firstDayOfWeek(currDate);
		Date lastFirstDayOfWeek = beforeDate(firstDayOfWeek, 7);
		return lastFirstDayOfWeek;
	}

	/**
	 * 
	 * <b>描述:上上周的第一天</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date firstDayOfLastLastWeek(Date currDate) {
		Date firstDayOfWeek = firstDayOfWeek(currDate);
		Date lastLastFirstDayOfWeek = beforeDate(firstDayOfWeek, 14);
		return lastLastFirstDayOfWeek;
	}

	/**
	 * 
	 * <b>描述:前几个月的第一天，0表示本月第一天</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @param months
	 * @return
	 */
	public static Date firstDayOfCustMonth(Date currDate, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONDAY);
		calendar.set(year, month, 1, 0, 0, 0);// 本月第一天
		int val = months / 12;// 商
		int rem = months % 12;// 余数
		year = year - val;
		month = month - rem;
		calendar.set(year, month, 1, 0, 0, 0);
		return calendar.getTime();
	}

	public static Date afterDate(Date currDate, long differMillis) {
		Calendar calendar = Calendar.getInstance();
		long total = currDate.getTime() + differMillis;
		calendar.setTimeInMillis(total);
		return calendar.getTime();
	}

	public static long differMillis(Date startDate, Date endDate) {
		long millis = endDate.getTime() - startDate.getTime();
		return millis;
	}

	/**
	 * 
	 * <b>描述:本月的第一天，也是上月最后一天</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date firstDayOfMonth(Date currDate) {
		return firstDayOfCustMonth(currDate, 0);
	}

	/**
	 * 
	 * <b>描述:上个月的第一天</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date firstDayOfLastMonth(Date currDate) {
		return firstDayOfCustMonth(currDate, 1);
	}

	/**
	 * 
	 * <b>描述:上上个月第一天</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param currDate
	 * @return
	 */
	public static Date firstDayOfLastLastMonth(Date currDate) {
		return firstDayOfCustMonth(currDate, 2);
	}

	/**
	 * 
	 * <b>描述: 相差多少天</b>: <blockquote>
	 * 
	 * <pre>
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int differDays(Date startDate, Date endDate) {
		long days = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
		return (int) days;
	}

	public static void main(String args[]) {
		Date currDate = new Date();
		log.info(sdf.format(currDate));
		
		log.info(sdf.format(todayStart(currDate)));
		log.info(sdf.format(todayEnd(currDate)));
		
		log.info(sdf.format(hourStart(currDate)));
		log.info(sdf.format(hourEnd(currDate)));

	}

}
