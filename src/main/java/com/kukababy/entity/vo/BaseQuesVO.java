/**
 * 
 */
package com.kukababy.entity.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>描述:题型的基本属性</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian.wang@ezcharting.cn
 * @date 2017年8月7日 下午2:52:23
 */

public class BaseQuesVO {
	
	private String quesId;
	private String varName;
	private String fieldName;
	private String varNameT;
	private String fieldNameT;
	private List<BaseItemVO> rows = new ArrayList();
	private List<BaseItemVO> cols = new ArrayList();
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
	 * @return the varNameT
	 */
	public String getVarNameT() {
		return varNameT;
	}
	/**
	 * @param varNameT the varNameT to set
	 */
	public void setVarNameT(String varNameT) {
		this.varNameT = varNameT;
	}
	/**
	 * @return the fieldNameT
	 */
	public String getFieldNameT() {
		return fieldNameT;
	}
	/**
	 * @param fieldNameT the fieldNameT to set
	 */
	public void setFieldNameT(String fieldNameT) {
		this.fieldNameT = fieldNameT;
	}
	/**
	 * @return the rows
	 */
	public List<BaseItemVO> getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<BaseItemVO> rows) {
		this.rows = rows;
	}
	/**
	 * @return the cols
	 */
	public List<BaseItemVO> getCols() {
		return cols;
	}
	/**
	 * @param cols the cols to set
	 */
	public void setCols(List<BaseItemVO> cols) {
		this.cols = cols;
	}
	
	
	

}
