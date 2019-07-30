package com.zxk175.well.config.filter;

import cn.hutool.core.util.ArrayUtil;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.FilterUtil;
import com.zxk175.well.common.util.MyStrUtil;
import com.zxk175.well.common.util.common.CommonUtil;
import com.zxk175.well.common.util.jwt.JwtAuthUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (FilterUtil.cancelFilter(httpRequest)) {
            filterChain.doFilter(request, response);
        } else {
            // 开始进入请求地址拦截
            boolean flag = false;
            Cookie[] cookies = httpRequest.getCookies();
            if (ArrayUtil.isNotEmpty(cookies)) {
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    // 可在此处作Token校验
                    if (Const.TOKEN_KEY.equals(name)) {
                        try {
                            flag = true;
                            JwtAuthUtil.parserSysJwt(cookie.getValue());
                        } catch (Exception e) {
                            flag = false;
                        }
                    }
                }
            }

            if (flag) {
                filterChain.doFilter(request, response);
            } else {
                String myHost = httpRequest.getHeader(Const.MY_HOST);
                String fullUrl = (MyStrUtil.isBlank(myHost) ? "" : myHost) + CommonUtil.getLoginUrl(httpRequest);
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendRedirect(fullUrl);
            }
        }
    }
}
