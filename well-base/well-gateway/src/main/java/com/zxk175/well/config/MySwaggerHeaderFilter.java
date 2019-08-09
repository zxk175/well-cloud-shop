package com.zxk175.well.config;

import com.zxk175.well.common.util.MyStrUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * @author zxk175
 */
@Component
public class MySwaggerHeaderFilter extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, filterChain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (MyStrUtil.neEndWithIgnoreCase(path, MySwaggerProvider.API_URI)) {
                return filterChain.filter(exchange);
            }

            String headerName = "X-Forwarded-Prefix";
            String basePath = path.substring(0, path.lastIndexOf(MySwaggerProvider.API_URI));
            ServerHttpRequest newRequest = request.mutate().header(headerName, basePath).build();
            return filterChain.filter(exchange.mutate().request(newRequest).build());
        };
    }
}