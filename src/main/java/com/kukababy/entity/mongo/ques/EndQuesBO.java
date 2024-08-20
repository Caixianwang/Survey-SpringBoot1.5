/**
 * 
 */
package com.kukababy.entity.mongo.ques;

import java.util.ArrayList;
import java.util.List;

import com.kukababy.entity.mongo.bo.BaseQuesBO;

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

public class EndQuesBO extends BaseQuesBO {
	private String varName = "end";
	private String fieldName = "end";
	private List<EndItemBO> rows = new ArrayList();

	public EndQuesBO() {
		this.quesType = BaseQuesBO.endType;
		this.htmlTitle="结束题";
	}

	/**
	 * @return the varName
	 */
	public String getVarName() {
		return varName;
	}

	/**
	 * @param varName
	 *            the varName to set
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
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the rows
	 */
	public List<EndItemBO> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<EndItemBO> rows) {
		this.rows = rows;
	}

}
