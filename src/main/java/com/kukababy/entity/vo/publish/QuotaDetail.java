/**
 * 
 */
package com.kukababy.entity.vo.publish;

import java.util.LinkedHashSet;

import com.alibaba.fastjson.JSON;

/**
 * <b>描述</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年8月7日 下午2:52:23
 */
public class QuotaDetail {

	private String fieldName;
	private String fieldVal;
	private long maxSize;
	private long realSize;
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the fieldVal
	 */
	public String getFieldVal() {
		return fieldVal;
	}
	/**
	 * @param fieldVal the fieldVal to set
	 */
	public void setFieldVal(String fieldVal) {
		this.fieldVal = fieldVal;
	}
	/**
	 * @return the maxSize
	 */
	public long getMaxSize() {
		return maxSize;
	}
	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
	/**
	 * @return the realSize
	 */
	public long getRealSize() {
		return realSize;
	}
	/**
	 * @param realSize the realSize to set
	 */
	public void setRealSize(long realSize) {
		this.realSize = realSize;
	}

	
}
