package com.zxk175.well.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author zxk175
 * @since 2018/12/30 18:11
 */
@Component
public class MyZuulFilter extends ZuulFilter {

    /**
     * filter类型
     * <p>
     * pre:请求执行之前filter
     * route: 处理请求，进行路由
     * post: 请求处理完成后执行的filter
     * error:出现错误时执行的filter
     *
     * @return ignore
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * filter执行顺序，通过数字指定
     *
     * @return ignore
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * filter是否需要执行 true执行 false 不执行
     *
     * @return ignore
     */
    @Override
    public boolean shouldFilter() {
        return false;
    }

    /**
     * filter具体逻辑
     *
     * @return ignore
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}