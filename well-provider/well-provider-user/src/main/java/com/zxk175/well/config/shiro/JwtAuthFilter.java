package com.zxk175.well.config.shiro;

import com.github.sd4324530.jtuple.Tuple2;
import com.github.sd4324530.jtuple.Tuples;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.util.MyStrUtil;
import com.zxk175.well.common.util.json.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zxk175
 * @since 2019/03/23 20:27
 */
@Slf4j
public class JwtAuthFilter extends AuthenticatingFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        // 获取请求token
        String token = getRequestToken(request);

        if (MyStrUtil.isBlank(token)) {
            return null;
        }

        return new JwtToken(token);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Tuple2<HttpServletRequest, HttpServletResponse> tuple = setResponse(request, response, true);
        HttpServletResponse httpResponse = tuple.second;

        // 获取请求token，如果token不存在，直接返回401
        String token = getRequestToken(request);
        if (MyStrUtil.isBlank(token)) {
            String json = FastJsonUtil.jsonStrByMy(Response.fail(401, "拒绝访问，Token为空"));
            httpResponse.getWriter().print(json);

            return false;
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        Tuple2<HttpServletRequest, HttpServletResponse> tuple = setResponse(request, response, true);
        HttpServletResponse httpResponse = tuple.second;

        try {
            // 处理登录失败的异常
            String json = FastJsonUtil.jsonStrByMy(Response.fail(401, "登录失败，无效Token"));
            httpResponse.getWriter().print(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // 对跨域提供支持
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Tuple2<HttpServletRequest, HttpServletResponse> tuple = setResponse(request, response, false);
        HttpServletRequest httpRequest = tuple.first;
        HttpServletResponse httpResponse = tuple.second;

        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (RequestMethod.OPTIONS.name().equals(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }

    private String getRequestToken(ServletRequest servletRequest) {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        // 从header中获取token
        String token = httpRequest.getHeader(Const.TOKEN_KEY);

        // 如果header中不存在token，则从参数中获取token
        if (MyStrUtil.isBlank(token)) {
            token = httpRequest.getParameter(Const.TOKEN_KEY);
        }

        return token;
    }

    private Tuple2<HttpServletRequest, HttpServletResponse> setResponse(ServletRequest request, ServletResponse response, boolean isJson) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String origin = httpRequest.getHeader("Origin");
        httpResponse.setHeader("Access-control-Allow-Origin", origin);

        String header = httpRequest.getHeader("Access-Control-Request-Headers");
        httpResponse.setHeader("Access-Control-Allow-Headers", header);

        httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        if (isJson) {
            httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }

        return Tuples.tuple(httpRequest, httpResponse);
    }
}
