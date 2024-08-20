package com.kukababy.exception;

import java.io.Serializable;

/**
 * @describe 描述
 * @author caixian_wang@sina.com
 * @date 创建时间：2017年4月28日 下午4:15:03
 * 
 */
public class BizException extends Exception implements Serializable {

    private String errCode;
    private String zhMsg;
    private String enMsg;

    public BizException() {
        super();
    }

    /**
     *
     * @param errCode
     * @param zhMsg
     * @param enMsg
     */
    public BizException(String errCode, String zhMsg, String enMsg) {
        super(zhMsg);
        this.errCode = errCode;
        this.zhMsg = zhMsg;
        this.enMsg = enMsg;
    }

    /**
     *
     * @param errCode
     * @param zhMsg
     */
    public BizException(String errCode, String zhMsg) {
        super(zhMsg);
        this.errCode = errCode;
        this.zhMsg = zhMsg;
    }

    /**
     *
     * @param zhMsg
     */
    public BizException(String zhMsg) {
        super(zhMsg);
        this.zhMsg = zhMsg;
    }

    /**
     *
     * @param errCode
     * @param errMsg
     * @param cause
     */
    public BizException(String errCode, String errMsg, Throwable cause) {
        super(errMsg, cause);
        this.errCode = errCode;
        this.zhMsg = zhMsg;

    }

    /**
     * @param errMsg
     * @param cause
     */
    public BizException(String errMsg, Throwable cause) {
        super(errMsg, cause);
        this.zhMsg = zhMsg;

    }

    /**
     * @param cause
     */
    public BizException(Throwable cause) {
        super(cause);
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getZhMsg() {
        return zhMsg;
    }

    public void setZhMsg(String zhMsg) {
        this.zhMsg = zhMsg;
    }

    public String getEnMsg() {
        return enMsg;
    }

    public void setEnMsg(String enMsg) {
        this.enMsg = enMsg;
    }

}
