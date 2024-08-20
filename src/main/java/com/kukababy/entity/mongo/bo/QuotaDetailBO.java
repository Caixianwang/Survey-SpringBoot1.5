/**
 * 
 */
package com.kukababy.entity.mongo.bo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

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
public class QuotaDetailBO {
	/**
	 * 配额明细内容 set长度为2，第一个 quesId,第2个是（按逗号分隔） varName,val
	 * 主要用于单选、多选
	 */
	private List<String> contents = new ArrayList();
	private long maxSize;
	private long realSize;
	/**
	 * @return the contents
	 */
	public List<String> getContents() {
		return contents;
	}
	/**
	 * @param contents the contents to set
	 */
	public void setContents(List<String> contents) {
		this.contents = contents;
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
