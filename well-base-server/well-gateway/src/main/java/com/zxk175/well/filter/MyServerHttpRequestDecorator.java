package com.zxk175.well.filter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.util.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxk175
 * @since 2019/07/25 16:18
 */
public class MyServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    private final List<DataBuffer> dataBuffers = new ArrayList<>();


    MyServerHttpRequestDecorator(ServerHttpRequest delegateRequest) {
        super(delegateRequest);
        super.getBody().map(this::apply).subscribe();
    }

    @NonNull
    @Override
    public Flux<DataBuffer> getBody() {
        return copy();
    }

    private Flux<DataBuffer> copy() {
        return Flux.fromIterable(dataBuffers).map(buf -> buf.factory().wrap(buf.asByteBuffer()));
    }

    private DataBuffer apply(DataBuffer dataBuffer) {
        dataBuffers.add(dataBuffer);
        return dataBuffer;
    }
}