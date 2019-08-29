package com.zxk175.well.module.service.gateway;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

/**
 * @author zxk175
 * @since 2019-08-29 15:30
 */
public interface DynamicRouteService {

    String save(RouteDefinition definition);

    String modify(RouteDefinition definition);

    Mono<ResponseEntity<Object>> remove(String id);
}