package com.codelnn.emms.common;

import lombok.Data;

/**
 * @description:
 * @author: znx
 * @create: 2020-12-16 20:13
 **/
@Data
public class CommonResult {

    /**
     * http状态码
     * 200 操作成功 -1 操作失败
     */
    private int code;

    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回的数据
     */
    private Object data;

    public CommonResult() {
    }

    public CommonResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static CommonResult error(String message){
        CommonResult commonResult = new CommonResult();
        commonResult.setMsg(message);
        commonResult.setCode(-1);
        return commonResult;
    }


    public static CommonResult error(int code,String message){
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(code);
        commonResult.setMsg(message);
        return commonResult;
    }

    public static CommonResult success(Object data){
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("操作成功");
        commonResult.setData(data);
        return commonResult;
    }

    public static CommonResult success(String message){
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg(message);
        commonResult.setData(null);
        return commonResult;
    }


    public static CommonResult success(){
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(200);
        commonResult.setMsg("操作成功");
        commonResult.setData(null);
        return commonResult;
    }


}
