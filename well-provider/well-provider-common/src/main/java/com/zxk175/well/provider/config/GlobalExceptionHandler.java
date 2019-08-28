package com.zxk175.well.provider.config;

import com.zxk175.well.base.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zxk175
 * @since 2019-08-28 11:18
 */
@Slf4j
@Order(-1000)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception ex) {
        ex.printStackTrace();
        return Response.fail("未知异常");
    }
}