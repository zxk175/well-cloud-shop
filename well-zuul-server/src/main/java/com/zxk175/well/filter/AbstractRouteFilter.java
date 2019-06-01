package com.zxk175.well.filter;

import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zxk175
 * @since 2019/06/01 14:42
 * <p>
 * https://my.oschina.net/u/3226414/blog/1929936
 */
public abstract class AbstractRouteFilter extends ZuulFilter {

    private final RouteLocator routeLocator;
    private final UrlPathHelper urlPathHelper;


    AbstractRouteFilter(RouteLocator routeLocator, UrlPathHelper urlPathHelper) {
        this.routeLocator = routeLocator;
        this.urlPathHelper = urlPathHelper;
    }

    protected Route route(HttpServletRequest request) {
        String requestURI = urlPathHelper.getPathWithinApplication(request);
        return routeLocator.getMatchingRoute(requestURI);
    }
}
