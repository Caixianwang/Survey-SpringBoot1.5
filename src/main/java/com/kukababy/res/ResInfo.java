/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kukababy.res;

/**
 * @describe 描述
 * @author caixian_wang@sina.com
 * @date 创建时间：2015年4月22日 下午4:15:03
 * 
 */
public class ResInfo {
	public static final String SUCCESS = "Y";//
	public static final String FAIL = "F";// 业务操作失败
	public static final String OUT = "O";// session过期
	public static final String TIP = "T";// 答题提示
	public static final String ERROR = "E";// 系统错误
	private String tipsId ;//答题错误的ID
	private String code;
	private String msg;
	private String url;
	private String status = SUCCESS;
	private boolean hasError ;

	/**
	 * 返回前端的数据
	 */
	private Object res;

	public String getCode() {
		return code;
	}

	public ResInfo setCode(String code) {
		this.code = code;
		return this;
	}


	public String getStatus() {
		return status;
	}

	public ResInfo setStatus(String status) {
		this.status = status;
		return this;
	}

	

	public String getUrl() {
		return url;
	}

	public ResInfo setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @return the res
	 */
	public Object getRes() {
		return res;
	}

	/**
	 * @param res
	 *            the res to set
	 */
	public ResInfo setRes(Object res) {
		this.res = res;
		return this;
	}


	/**
	 * @return the hasError
	 */
	public boolean isHasError() {
		return hasError;
	}

	/**
	 * @param hasError the hasError to set
	 */
	public ResInfo setHasError(boolean hasError) {
		this.hasError = hasError;
		return this;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public ResInfo setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	/**
	 * @return the tipsId
	 */
	public String getTipsId() {
		return tipsId;
	}

	/**
	 * @param tipsId the tipsId to set
	 */
	public void setTipsId(String tipsId) {
		this.tipsId = tipsId;
	}
	
	


}
