package com.kukababy.exception;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @describe 描述
 * @author caixian_wang@sina.com
 * @date 2014-3-26
 */
public class DatiException extends RuntimeException implements Serializable {
	private static Log log = LogFactory.getLog(DatiException.class);
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String code ;
    private String errMsg ;

    public DatiException() {
        super();
    }

    /**
	 * @param errMsg
	 */
	public DatiException(String errMsg) {
		super();
		this.errMsg = errMsg;
	}
	/**
	 * @param code
	 * @param errMsg
	 */
	public DatiException(String code, String errMsg) {
		super();
		this.code = code;
		this.errMsg = errMsg;
	}

	

	
	/**
	 * @return the errMsg
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * @param errMsg the errMsg to set
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	
	
    
}
