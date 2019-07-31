package com.zxk175.well.filter.log;

import com.zxk175.well.filter.util.GatewayLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
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
public class WrapperRequestGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        // 在 NettyWriteResponseFilter 之前
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 20;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain filterChain) {
        ServerHttpRequest originRequest = exchange.getRequest();
        URI originRequestUri = originRequest.getURI();

        boolean isRecorder = GatewayLogUtil.isRecorder(originRequest, originRequestUri);
        if (isRecorder) {
            return filterChain.filter(exchange);
        }

        // 记录请求记录
        MyServerHttpRequestDecorator myRequestDecorator = new MyServerHttpRequestDecorator(originRequest);
        GatewayLogUtil.recorderRequest(myRequestDecorator);

        MyServerHttpResponseDecorator myResponseDecorator = new MyServerHttpResponseDecorator(exchange.getResponse());

        return filterChain.filter(exchange.mutate().request(myRequestDecorator).response(myResponseDecorator).build());
    }
}