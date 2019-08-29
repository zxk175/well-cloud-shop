package com.zxk175.well.module.model;

import lombok.Data;

import java.util.List;

/**
 * @author zxk175
 * @since 2019-08-29 16:41
 */
@Data
public class GatewayRouteDefinition {

    private String routeId;

    private String routeUri;

    private Integer routeOrder;

    private List<GatewayPredicateDefinition> predicates;

    private List<GatewayFilterDefinition> filters;
}