package com.example.hou.result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //自定义异常
    @ExceptionHandler(BusinessException.class)
    public Result systemExceptionHandler(BusinessException e) {
        log.error("BusinessException全局异常：{}",e);

        Result result = new Result();
        result.setCode(e.getCode());
        result.setMsg(e.getMsg());
        return result;

    }

    //权限管理重大debug  security.access.AccessDeniedException: 不允许访问
    /*    需要在全局异常处理里捕获AccessDeniedException   才不会变成全局异常
     * 捕捉AccessDeniedException，spring security抛出的无权限访问的异常信息
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        Result result = new Result();
        result.setCode(HttpStatus.FORBIDDEN.value());
        result.setMsg("无权限访问，请联系系统管理员！");
        return result;
    }





    //系统异常
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("Exception全局异常：{}",e);
        Result result = new Result();
        result.setMsg(e.getMessage()+e.getCause());
        return result;
    }


}
