package com.zxk175.well.config.json;

import com.alibaba.fastjson.JSON;
import com.zxk175.well.base.consts.Const;
import com.zxk175.well.base.util.json.FastJsonValueFilter;
import org.reactivestreams.Publisher;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Encoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zxk175
 * @since 2019-08-30 10:54
 */
@Configuration
public class MyFastJsonConfig {

    @Bean
    public CodecCustomizer jsonCodecCustomizer() {
        return configurer -> configurer.defaultCodecs().jackson2JsonEncoder(new JsonEncoder());
    }

    public static class JsonEncoder implements Encoder<Object> {

        @Override
        public boolean canEncode(@NonNull ResolvableType elementType, MimeType mimeType) {
            return mimeType.includes(MimeTypeUtils.APPLICATION_JSON);
        }

        @NonNull
        @Override
        public Flux<DataBuffer> encode(@NonNull Publisher<?> inputStream, @NonNull DataBufferFactory bufferFactory, @NonNull ResolvableType elementType, MimeType mimeType, Map<String, Object> hints) {
            if (inputStream instanceof Mono) {
                return Mono.from(inputStream)
                        .map(value -> encodeValue(value, bufferFactory))
                        .flux();
            }

            return Flux.from(inputStream)
                    .map(value -> encodeValue(value, bufferFactory));
        }

        @NonNull
        @Override
        public List<MimeType> getEncodableMimeTypes() {
            return Collections.singletonList(MimeTypeUtils.APPLICATION_JSON);
        }

        private DataBuffer encodeValue(Object value, DataBufferFactory bufferFactory) {
            DataBuffer buffer = bufferFactory.allocateBuffer();
            byte[] bytes = JSON.toJSONBytes(value, Const.serializeConfig(), new FastJsonValueFilter(), Const.serializerFeatures());
            buffer.write(bytes);
            return buffer;
        }
    }
}