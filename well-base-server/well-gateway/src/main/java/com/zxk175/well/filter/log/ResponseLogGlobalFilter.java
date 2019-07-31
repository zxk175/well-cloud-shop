package com.zxk175.well.filter.log;

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
 * @since 2019/07/25 16:18
 */
@Slf4j
@Component
public class ResponseLogGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        // 在向业务服务转发前执行 NettyRoutingFilter 或 WebClientHttpRoutingFilter
        return Ordered.LOWEST_PRECEDENCE - 10;
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

        return filterChain.filter(exchange.mutate().build());
    }
}