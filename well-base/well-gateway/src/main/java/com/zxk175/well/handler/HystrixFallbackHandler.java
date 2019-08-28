package com.zxk175.well.handler;

import com.zxk175.well.base.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/**
 * @author zxk175
 * @since 2019-08-27 10:32
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {

    @NonNull
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        Optional<Object> originUri = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        originUri.ifPresent(originalUri -> log.error("网关请求:{} 失败", originalUri));

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(Response.fail("服务异常")));
    }
}