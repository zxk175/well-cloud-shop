package com.zxk175.well.filter.log;

import com.zxk175.well.filter.util.GatewayLogUtil;
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
 * @since 2019-08-11 01:28
 */
@Component
public class RequestLogFilter implements GlobalFilter, Ordered {
    
    @Override
    public int getOrder() {
        return FilterConst.REQUEST_LOG_FILTER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain filterChain) {
        ServerHttpRequest originRequest = exchange.getRequest();
        URI originRequestUri = originRequest.getURI();

        boolean isRecorder = GatewayLogUtil.isRecorder(originRequest, originRequestUri);
        if (isRecorder) {
            return filterChain.filter(exchange);
        }

        // 记录原始请求
        GatewayLogUtil.recorderOriginRequest(exchange);

        return filterChain.filter(exchange).then(Mono.fromRunnable(() -> GatewayLogUtil.recorderResponse(exchange)));
    }
}