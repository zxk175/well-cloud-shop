package com.zxk175.well.filter;

import com.zxk175.well.filter.util.GatewayLogUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author zxk175
 * @since 2019/07/25 16:18
 */
@Component
public class WrapperResponseGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain filterChain) {
        ServerHttpRequest originRequest = exchange.getRequest();
        URI originRequestUri = originRequest.getURI();

        boolean isRecorder = GatewayLogUtil.isRecorder(originRequest, originRequestUri);
        if (isRecorder) {
            return filterChain.filter(exchange);
        }

        // 记录响应记录
        ServerHttpResponse originResponse = exchange.getResponse();
        MyServerHttpResponseDecorator myResponseDecorator = new MyServerHttpResponseDecorator(originResponse);
        GatewayLogUtil.recorderResponse(exchange, myResponseDecorator);

        return filterChain.filter(exchange.mutate().response(myResponseDecorator).build());
    }
}