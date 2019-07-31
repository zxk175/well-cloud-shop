package com.zxk175.well.filter;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxk175
 * @since 2019/07/25 16:18
 */
public class MyServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    private final List<DataBuffer> dataBuffers = new ArrayList<>();


    MyServerHttpResponseDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }

    @NonNull
    @Override
    public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
        return super.writeWith(body);
    }
}