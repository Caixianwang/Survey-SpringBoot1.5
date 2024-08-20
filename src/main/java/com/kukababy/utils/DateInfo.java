/**
 * 
 */
package com.kukababy.utils;

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

public class DateInfo {
	/**
	 * 2018
	 */
	private String year;
	/**
	 * 2018.1
	 */
	private String quarter;
	/**
	 * 2018.01
	 */
	private String month;
	/**
	 * 2018.01 2018年第1周
	 */
	private String week;
	/**
	 * 2018.01.01
	 */
	private String day;

	/**
	 * 
	 */
	public DateInfo() {
		super();
	}

	/**
	 * @param month
	 * @param week
	 * @param day
	 */
	public DateInfo(String year, String quarter, String month, String week, String day) {
		super();
		this.year = year;
		this.quarter = quarter;
		this.month = month;
		this.week = week;
		this.day = day;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return the week
	 */
	public String getWeek() {
		return week;
	}

	/**
	 * @param week
	 *            the week to set
	 */
	public void setWeek(String week) {
		this.week = week;
	}

	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}

	/**
	 * @param day
	 *            the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return the quarter
	 */
	public String getQuarter() {
		return quarter;
	}

	/**
	 * @param quarter
	 *            the quarter to set
	 */
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

}
