package com.zxk175.well.filter;

import com.github.sd4324530.jtuple.Tuple2;
import com.netflix.zuul.context.RequestContext;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.net.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zxk175
 * @since 2018/12/30 18:11
 */
@Slf4j
@Component
public class MyZuulFilter extends AbstractRouteFilter {

    public MyZuulFilter(RouteLocator routeLocator, UrlPathHelper urlPathHelper) {
        super(routeLocator, urlPathHelper);
    }

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
        return FilterConstants.PRE_TYPE;
    }

    /**
     * filter顺序，数字越小表示顺序越高，越先执行
     *
     * @return ignore
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * filter是否需要执行 true执行 false不执行
     *
     * @return ignore
     */
    @Override
    public boolean shouldFilter() {
        return true;
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

        Route route = route(request);
        Tuple2<String, String> tuple = RequestUtil.requestUrlWithTuple(request, false, false);
        log.info("send {}，request to {}", request.getMethod(), tuple.second);
        ctx.addZuulRequestHeader(Const.MY_HOST, route.getLocation());

        return null;
    }
}