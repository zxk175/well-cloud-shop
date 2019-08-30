package com.zxk175.well.module.service.gateway;

import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @author zxk175
 * @since 2019-08-29 15:30
 */
public interface DynamicRouteService {

    boolean save(RouteDefinition definition);

    boolean modify(RouteDefinition definition);

    boolean remove(String id);
}