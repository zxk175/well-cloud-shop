package com.zxk175.well.config;

import com.zxk175.well.handler.HystrixFallbackHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @author zxk175
 * @since 2019-08-27 10:42
 */
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {

    private final HystrixFallbackHandler hystrixFallbackHandler;


    @Bean
    public RouterFunction<?> routerFunction() {
        MediaType[] mediaTypes = new MediaType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8};
        RequestPredicate fallback = RequestPredicates.path("/fallback").and(RequestPredicates.accept(mediaTypes));

        return RouterFunctions.route(fallback, hystrixFallbackHandler);
    }
}