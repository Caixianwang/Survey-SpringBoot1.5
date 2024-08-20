/**
 * 
 */
package com.kukababy.entity.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kukababy.entity.mongo.bo.PageBO;
import com.kukababy.entity.mongo.bo.QuotaGroupBO;
import com.kukababy.entity.mongo.bo.SurveyCfgBO;
import com.kukababy.entity.vo.WxCfg;
import com.kukababy.utils.Consts;

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
@Document(collection = "m_seq")
public class SeqMO {
	public static final String TABLE_NAME = "m_seq";
	@Id
	private String tblId;
	private int seq;
	/**
	 * @return the tblId
	 */
	public String getTblId() {
		return tblId;
	}
	/**
	 * @param tblId the tblId to set
	 */
	public void setTblId(String tblId) {
		this.tblId = tblId;
	}
	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}
	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	
	
}
