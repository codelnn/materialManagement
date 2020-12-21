package com.codelnn.emms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:  业务异常
 * @author: znx
 * @create: 2020-12-16 21:03
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    protected int errorCode = -1;

    /**
     * 错误信息
     */
    protected String errorMsg;


    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
        this.errorMsg = message;
    }


    public ServiceException(BaseCodeInterface errorInfoInterface) {
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public ServiceException(BaseCodeInterface errorInfoInterface,String message) {
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = message;
    }
}
