package com.zxk175.well.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zxk175
 * @since 2018/12/27 17:48
 */
public class FilterUtil {

    public static Boolean cancelFilter(HttpServletRequest httpRequest) {
        // 得到请求地址
        String uri = httpRequest.getRequestURI();
        return uri.contains("/lpr/v1");
    }
}
