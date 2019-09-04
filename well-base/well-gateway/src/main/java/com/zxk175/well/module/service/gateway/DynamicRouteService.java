package com.zxk175.well.module.service.gateway;

import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @author zxk175
 * @since 2019-08-29 15:30
 */
public interface DynamicRouteService {

    /**
     * 添加路由
     *
     * @param definition ignore
     * @return ignore
     */
    boolean save(RouteDefinition definition);

    /**
     * 修改路由
     *
     * @param definition ignore
     * @return ignore
     */
    boolean modify(RouteDefinition definition);

    /**
     * 删除路由
     *
     * @param id ignore
     * @return ignore
     */
    boolean remove(String id);
}