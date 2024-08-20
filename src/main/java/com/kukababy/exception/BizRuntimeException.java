package com.kukababy.exception;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @describe 描述
 * @author caixian_wang@sina.com
 * @date 2014-3-26
 */
public class BizRuntimeException extends RuntimeException implements Serializable {

    private static Log log = LogFactory.getLog(BizRuntimeException.class);
    private static final long serialVersionUID = -6963187366089365790L;

    public BizRuntimeException() {
        super();
    }

    /**
     * @param code：错误代码或错误提示
     */
    public BizRuntimeException(String errorMsg) {
        super(errorMsg);
    }

    /**
     * @param message
     * @param cause
     */
    public BizRuntimeException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);

    }

    /**
     * @param cause
     */
    public BizRuntimeException(Throwable cause) {
        super(cause);
    }
}
