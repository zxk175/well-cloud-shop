package com.zxk175.well.common.util.net;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Maps;
import com.zxk175.well.common.util.MyStrUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author zxk175
 * @since 2017/2/21
 */
public class RequestUtil {

    public static HttpServletRequest request() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        throw new RuntimeException("RequestAttributes is null");
    }

    public static String requestUrl(HttpServletRequest request) {
        return requestUrl(request, false);
    }

    public static String requestUrl(HttpServletRequest request, boolean flag) {
        String requestUri;
        String queryString;

        if (flag) {
            String host = request.getHeader("Host");
            requestUri = request.getScheme() + "://" + host;
            requestUri = requestUri + Convert.toStr(request.getAttribute("javax.servlet.error.request_uri"));
            queryString = Convert.toStr(request.getAttribute("javax.servlet.forward.query_string"));
        } else {
            requestUri = request.getRequestURL().toString();
            queryString = request.getQueryString();
        }

        return requestUri + (MyStrUtil.isBlank(queryString) ? MyStrUtil.EMPTY : "?" + queryString);
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = Maps.newHashMap();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
