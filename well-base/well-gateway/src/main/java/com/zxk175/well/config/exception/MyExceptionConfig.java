package com.zxk175.well.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * @author zxk175
 * @since 2019-08-27 14:24
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ResourceProperties.class)
public class MyExceptionConfig {

    private final List<ViewResolver> viewResolvers;
    private final ServerProperties serverProperties;
    private final ResourceProperties resourceProperties;
    private final ApplicationContext applicationContext;
    private final ServerCodecConfigurer serverCodecConfigurer;


    @Autowired
    public MyExceptionConfig(ServerProperties serverProperties, ObjectProvider<List<ViewResolver>> viewResolvers, ResourceProperties resourceProperties, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        this.serverProperties = serverProperties;
        this.viewResolvers = viewResolvers.getIfAvailable(Collections::emptyList);
        this.resourceProperties = resourceProperties;
        this.applicationContext = applicationContext;
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes) {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler(errorAttributes, this.resourceProperties, this.serverProperties.getError(), this.applicationContext);
        exceptionHandler.setViewResolvers(this.viewResolvers);
        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());

        return exceptionHandler;
    }
}
