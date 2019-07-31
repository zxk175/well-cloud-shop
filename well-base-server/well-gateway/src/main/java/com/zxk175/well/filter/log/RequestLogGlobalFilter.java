package com.zxk175.well.filter.log;

import com.zxk175.well.common.consts.Const;
import com.zxk175.well.filter.util.GatewayLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.net.URI;

/**
 * @author zxk175
 * @since 2019/07/25 16:18
 */
@Slf4j
@Component
public class RequestLogGlobalFilter implements GlobalFilter, Ordered {

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
        MyRequestDecorator myRequestDecorator = new MyRequestDecorator(originRequest);
        // 原始请求
        GatewayLogUtil.recorderOriginRequest(exchange);
        // 代理请求
        GatewayLogUtil.recorderRouteRequest(exchange);

        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    Flux<DataBuffer> dataBufferFlux = fluxBody.map(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);

                        // 记录响应记录
                        String responseBody = new String(content, Const.UTF_8_OBJ);
                        GatewayLogUtil.recorderResponse(response, responseBody);

                        // 释放内存
                        DataBufferUtils.release(dataBuffer);

                        return bufferFactory.wrap(content);
                    });

                    return super.writeWith(dataBufferFlux);
                }

                return super.writeWith(body);
            }
        };

        return filterChain.filter(exchange.mutate().request(myRequestDecorator).response(decoratedResponse).build());
    }
}