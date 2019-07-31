package com.zxk175.well.filter;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
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
        DataBufferFactory bufferFactory = this.bufferFactory();
        if (body instanceof Flux) {
            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
            return super.writeWith(fluxBody.map(dataBuffer -> {
                dataBuffers.add(dataBuffer);

                byte[] content = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(content);
                // 释放掉内存
                DataBufferUtils.release(dataBuffer);

                return bufferFactory.wrap(content);
            }));
        }

        return super.writeWith(body);
    }

    @NonNull
    @Override
    public Mono<Void> writeAndFlushWith(@NonNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return writeWith(Flux.from(body).flatMapSequential(p -> p));
    }

    public Flux<DataBuffer> copy() {
        return Flux.fromIterable(dataBuffers).map(buffer -> buffer.factory().wrap(buffer.asByteBuffer()));
    }
}