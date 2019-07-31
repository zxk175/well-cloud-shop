package com.zxk175.well.filter.log;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
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
public class MyResponseDecorator extends ServerHttpResponseDecorator {

    private final List<DataBuffer> dataBuffers = new ArrayList<>();


    MyResponseDecorator(ServerHttpResponse delegateResponse) {
        super(delegateResponse);
    }

    @NonNull
    @Override
    public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
        return DataBufferUtils.join(Flux.from(body))
                .doOnNext(this.dataBuffers::add)
                .flatMap(dataBuffer -> super.writeWith(copy()));
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