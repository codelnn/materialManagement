package com.codelnn.emms.exception;

import com.codelnn.emms.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @description:  全局异常处理类
 * @author: znx
 * @create: 2020-12-16 21:05
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *  处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public CommonResult BindExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return CommonResult.error(message);
    }

    /**
     *  处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public CommonResult ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return CommonResult.error(message);
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public CommonResult MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return CommonResult.error(message);
    }

    /**
     * 处理servlet异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServletException.class)
    @ResponseBody
    public  CommonResult servletExceptionHandler(HttpServletRequest req, ServletException e){
        log.error("web服务器异常 {}",e.getMessage());
        return CommonResult.error(e.getMessage());
    }

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public  CommonResult bizExceptionHandler(HttpServletRequest req, ServiceException e){
        log.error("业务异常=>{}",e.getErrorMsg());
        return CommonResult.error(e.getErrorCode(),e.getErrorMsg());
    }

    /**
     * 捕捉其他所有异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult globalException(HttpServletRequest request, Throwable ex) {
        String message = ex.getMessage();
        log.error("系统异常=>{}",ex.getMessage());
        return new CommonResult(getStatus(request).value(), "系统异常"+message, null);
    }

    

    /**
     * 获取状态码
     * @param request
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
    
    
    
    
}
