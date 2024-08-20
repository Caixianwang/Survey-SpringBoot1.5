/**
 * 
 */
package com.kukababy.entity.mongo.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <b>描述:题型的基本属性</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2017年8月7日 下午2:52:23
 */

public class ElBO {
	public static final String or = "||";
	public static final String and = "&&";
	public static final String el = "el";
	private String type;
	private String quesId;
	private String varName;
	private String op;
	private String val;
	private List<ElBO> childs;
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the quesId
	 */
	public String getQuesId() {
		return quesId;
	}
	/**
	 * @param quesId the quesId to set
	 */
	public void setQuesId(String quesId) {
		this.quesId = quesId;
	}
	/**
	 * @return the varName
	 */
	public String getVarName() {
		return varName;
	}
	/**
	 * @param varName the varName to set
	 */
	public void setVarName(String varName) {
		this.varName = varName;
	}
	/**
	 * @return the op
	 */
	public String getOp() {
		return op;
	}
	/**
	 * @param op the op to set
	 */
	public void setOp(String op) {
		this.op = op;
	}
	/**
	 * @return the val
	 */
	public String getVal() {
		return val;
	}
	/**
	 * @param val the val to set
	 */
	public void setVal(String val) {
		this.val = val;
	}
	/**
	 * @return the childs
	 */
	public List<ElBO> getChilds() {
		return childs;
	}
	/**
	 * @param childs the childs to set
	 */
	public void setChilds(List<ElBO> childs) {
		this.childs = childs;
	}
	
	

}
