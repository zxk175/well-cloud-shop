package com.zxk175.well.filter.log;

import cn.hutool.core.util.ObjectUtil;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.filter.util.GatewayLogUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Gateway Context Filter
 *
 * @author chenggang
 * @date 2019/01/29
 */
@Slf4j
@AllArgsConstructor
@Component
public class GatewayContextFilter implements GlobalFilter, Ordered {

    private static final List<HttpMessageReader<?>> MESSAGE_READERS = HandlerStrategies.withDefaults().messageReaders();


    @Override
    public int getOrder() {
        return FilterConst.GATEWAY_CONTEXT_FILTER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain filterChain) {
        ServerHttpRequest request = exchange.getRequest();

        // 将GatewayContext保存到Exchange中
        GatewayContext gatewayContext = new GatewayContext();
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, gatewayContext);

        HttpHeaders headers = request.getHeaders();
        MediaType contentType = headers.getContentType();
        if (headers.getContentLength() > 0) {
            if (MediaType.APPLICATION_JSON.equals(contentType) || MediaType.APPLICATION_JSON_UTF8.equals(contentType)) {
                return readBody(exchange, filterChain, gatewayContext);
            }

            if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
                return readFormData(exchange, filterChain, gatewayContext);
            }
        }

        return filterChain.filter(exchange);
    }

    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain filterChain, GatewayContext gatewayContext) {
        ServerHttpRequest httpRequest = exchange.getRequest();

        return DataBufferUtils.join(httpRequest.getBody())
                .flatMap(dataBuffer -> {
                    Flux<DataBuffer> cachedFlux = GatewayLogUtil.packByte(exchange, dataBuffer);

                    // 重新包装 ServerHttpRequest
                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(httpRequest) {
                        @NonNull
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return cachedFlux;
                        }
                    };

                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    return ServerRequest.create(mutatedExchange, MESSAGE_READERS)
                            .bodyToMono(String.class)
                            .doOnNext(gatewayContext::setRequestBody)
                            .then(filterChain.filter(mutatedExchange));
                });
    }

    private Mono<Void> readFormData(ServerWebExchange exchange, GatewayFilterChain filterChain, GatewayContext gatewayContext) {
        HttpHeaders headers = exchange.getRequest().getHeaders();

        return exchange.getFormData()
                .doOnNext(gatewayContext::setFormData)
                .then(Mono.defer(() -> {
                    // formData 为空，直接返回
                    MultiValueMap<String, String> formData = gatewayContext.getFormData();
                    if (null == formData || formData.isEmpty()) {
                        return filterChain.filter(exchange);
                    }

                    Charset charset;
                    String charsetName;
                    MediaType contentType = headers.getContentType();
                    if (ObjectUtil.isNotNull(contentType)) {
                        charset = contentType.getCharset();
                        charset = charset == null ? Const.UTF_8_OBJ : charset;
                        charsetName = charset.name();
                    } else {
                        charset = Const.UTF_8_OBJ;
                        charsetName = Const.UTF_8;
                    }

                    StringBuilder formDataBodyBuilder = new StringBuilder();
                    try {
                        // 重新包装 form data
                        String entryKey;
                        List<String> entryValue;
                        for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
                            entryKey = entry.getKey();
                            entryValue = entry.getValue();
                            if (entryValue.size() > 1) {
                                for (String value : entryValue) {
                                    formDataBodyBuilder.append(entryKey).append("=").append(URLEncoder.encode(value, charsetName)).append("&");
                                }
                            } else {
                                formDataBodyBuilder.append(entryKey).append("=").append(URLEncoder.encode(entryValue.get(0), charsetName)).append("&");
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    // 去除最后一个 '&'
                    String formDataBodyString = "";
                    if (formDataBodyBuilder.length() > 0) {
                        formDataBodyString = formDataBodyBuilder.substring(0, formDataBodyBuilder.length() - 1);
                    }

                    byte[] bodyBytes = formDataBodyString.getBytes(charset);
                    int contentLength = bodyBytes.length;
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(exchange.getRequest().getHeaders());
                    httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
                    httpHeaders.setContentLength(contentLength);

                    BodyInserter<String, ReactiveHttpOutputMessage> bodyInsert = BodyInserters.fromObject(formDataBodyString);
                    CachedBodyOutputMessage cachedBodyOutputMessage = new CachedBodyOutputMessage(exchange, httpHeaders);

                    return bodyInsert.insert(cachedBodyOutputMessage, new BodyInserterContext())
                            .then(Mono.defer(() -> {
                                ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                                    @NonNull
                                    @Override
                                    public HttpHeaders getHeaders() {
                                        return httpHeaders;
                                    }

                                    @NonNull
                                    @Override
                                    public Flux<DataBuffer> getBody() {
                                        return cachedBodyOutputMessage.getBody();
                                    }
                                };

                                return filterChain.filter(exchange.mutate().request(decorator).build());
                            }));
                }));
    }
}