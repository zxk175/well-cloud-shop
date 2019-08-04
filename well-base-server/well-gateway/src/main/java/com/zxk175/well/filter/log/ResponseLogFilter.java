package com.zxk175.well.filter.log;

import cn.hutool.core.util.ObjectUtil;
import com.zxk175.well.filter.util.GatewayLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultClientResponse;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

/**
 * @author zxk175
 */
@Slf4j
@Component
public class ResponseLogFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return FilterConst.RESPONSE_DATA_FILTER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain filterChain) {
        ServerHttpResponse httpResponse = exchange.getResponse();

        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(httpResponse) {
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                return DataBufferUtils.join(Flux.from(body))
                        .flatMap(dataBuffer -> {
                            Flux<DataBuffer> cachedFlux = GatewayLogUtil.packByte(exchange, dataBuffer);

                            HttpHeaders httpHeaders = httpResponse.getHeaders();
                            DefaultClientResponse clientResponse = new DefaultClientResponse(new ResponseAdapter(cachedFlux, httpHeaders), ExchangeStrategies.withDefaults());
                            BodyInserter<Flux<DataBuffer>, ReactiveHttpOutputMessage> bodyInsert = BodyInserters.fromDataBuffers(cachedFlux);
                            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, httpHeaders);

                            MediaType contentType = clientResponse.headers().contentType().orElse(MediaType.APPLICATION_OCTET_STREAM);
                            if (contentType.equals(MediaType.APPLICATION_JSON) || contentType.equals(MediaType.APPLICATION_JSON_UTF8)) {
                                return clientResponse.bodyToMono(String.class)
                                        .doOnNext(originBody -> {
                                            GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
                                            if (ObjectUtil.isNotNull(gatewayContext)) {
                                                gatewayContext.setResponseBody(originBody);
                                            }
                                        }).then(getDefer(cachedFlux, bodyInsert, outputMessage));
                            }

                            return getDefer(cachedFlux, bodyInsert, outputMessage);
                        });
            }

            @Override
            public Mono<Void> writeAndFlushWith(@NonNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }

            private Mono<Void> getDefer(Flux<DataBuffer> cachedFlux, BodyInserter<Flux<DataBuffer>, ReactiveHttpOutputMessage> bodyInsert, CachedBodyOutputMessage outputMessage) {
                return Mono.defer(() -> bodyInsert.insert(outputMessage, new BodyInserterContext())
                        .then(Mono.defer(() -> {
                            Flux<DataBuffer> messageBody = cachedFlux;
                            HttpHeaders headers = getDelegate().getHeaders();
                            if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                messageBody = messageBody.doOnNext(data -> headers.setContentLength(data.readableByteCount()));
                            }

                            return getDelegate().writeWith(messageBody);
                        })));
            }
        };

        return filterChain.filter(exchange.mutate().response(responseDecorator).build());
    }

    public static class ResponseAdapter implements ClientHttpResponse {

        private final Flux<DataBuffer> flux;
        private final HttpHeaders headers;


        ResponseAdapter(Publisher<? extends DataBuffer> body, HttpHeaders headers) {
            this.headers = headers;
            if (body instanceof Flux) {
                flux = Flux.from(body);
            } else {
                flux = ((Mono) body).flux();
            }
        }

        @NonNull
        @Override
        public Flux<DataBuffer> getBody() {
            return flux;
        }

        @NonNull
        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        @Override
        public HttpStatus getStatusCode() {
            return null;
        }

        @Override
        public int getRawStatusCode() {
            return 0;
        }

        @Override
        public MultiValueMap<String, ResponseCookie> getCookies() {
            return null;
        }
    }
}