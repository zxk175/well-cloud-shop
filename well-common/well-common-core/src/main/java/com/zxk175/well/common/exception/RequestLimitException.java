package com.zxk175.well.common.exception;

/**
 * @author zxk175
 * @since 2019/05/07 14:06
 */
public class RequestLimitException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RequestLimitException() {
        super("HTTP请求超出设定的限制");
    }
}
