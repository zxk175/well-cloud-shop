package com.zxk175.well.filter.log;

import com.zxk175.well.common.util.MyStrUtil;
import com.zxk175.well.config.MySwaggerProvider;
import com.zxk175.well.filter.util.GatewayLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author zxk175
 */
@Slf4j
@Component
public class RouteRequestLogFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return FilterConst.ROUTE_REQUEST_LOG_FILTER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain filterChain) {
        ServerHttpRequest originRequest = exchange.getRequest();
        URI originRequestUri = originRequest.getURI();

        boolean isRecorder = GatewayLogUtil.isRecorder(originRequest, originRequestUri);
        if (isRecorder) {
            return filterChain.filter(exchange);
        }

        // 记录代理请求
        GatewayLogUtil.recorderRouteRequest(exchange);

        String path = originRequest.getURI().getPath();
        if (MyStrUtil.neEndWithIgnoreCase(path, MySwaggerProvider.API_URI)) {
            return filterChain.filter(exchange);
        }

        String headerName = "X-Forwarded-Prefix";
        String basePath = path.substring(0, path.lastIndexOf(MySwaggerProvider.API_URI));
        ServerHttpRequest newRequest = originRequest.mutate().header(headerName, basePath).build();

        return filterChain.filter(exchange.mutate().request(newRequest).build());
    }
}