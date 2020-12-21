package com.codelnn.emms.exception;

/**
 * @description: 自定义的错误描述枚举类需实现该接口
 * @author: znx
 * @create: 2020-12-16 20:59
 **/
public interface BaseCodeInterface {

    /**
     * 错误码
     * @return
     */
    int getResultCode();
    /**
     * 错误描述
     * @return
     */
    String getResultMsg();
}
