package com.zxk175.well.core.util.net;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Maps;
import com.zxk175.well.base.util.MyStrUtil;
import com.zxk175.well.base.util.tuple.Tuple2;
import com.zxk175.well.base.util.tuple.Tuples;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author zxk175
 * @since 2019-08-28 11:43
 */
public class RequestUtil {

    public static HttpServletRequest request() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        throw new RuntimeException("RequestAttributes is null");
    }

    public static String requestUrl(boolean isUri, boolean isError) {
        final HttpServletRequest request = request();
        return requestUrl(request, isUri, isError);
    }

    public static String requestUrl(HttpServletRequest request) {
        return requestUrl(request, false, false);
    }

    public static String requestUrl(HttpServletRequest request, boolean isError) {
        return requestUrl(request, false, isError);
    }

    public static String requestUrl(HttpServletRequest request, boolean isUri, boolean isError) {
        Tuple2<String, String> tuple = requestUrlWithTuple(request, isUri, isError);
        return tuple.second;
    }

    public static Tuple2<String, String> requestUrlWithTuple(HttpServletRequest request, boolean isUri, boolean isError) {
        String requestUri;
        String queryString;

        String headerHost = request.getHeader("Host");
        String host = request.getScheme() + "://" + headerHost;

        if (isError) {
            String uri = Convert.toStr(request.getAttribute("javax.servlet.error.request_uri"), "");
            if (isUri) {
                requestUri = uri;
            } else {
                requestUri = host + uri;
            }

            queryString = Convert.toStr(request.getAttribute("javax.servlet.forward.query_string"));
        } else {
            requestUri = isUri ? request.getRequestURI() : request.getRequestURL().toString();
            queryString = request.getQueryString();
        }

        return Tuples.tuple(host, requestUri + (MyStrUtil.isBlank(queryString) ? MyStrUtil.EMPTY : "?" + queryString));
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
