package com.zxk175.well.module.model;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author zxk175
 * @since 2019-08-29 16:41
 */
@Data
public class GatewayRouteDefinitionParamSave {

    private String routeId;

    private String routeUri;

    private Integer routeOrder;

    private List<GatewayPredicateDefinition> predicates;

    private List<GatewayFilterDefinition> filters;


    public List<GatewayPredicateDefinition> getPredicates() {
        return CollUtil.isEmpty(predicates) ? Collections.emptyList() : predicates;
    }

    public List<GatewayFilterDefinition> getFilters() {
        return CollUtil.isEmpty(filters) ? Collections.emptyList() : filters;
    }
}