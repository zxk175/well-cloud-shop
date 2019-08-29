package com.zxk175.well.module.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zxk175
 * @since 2019-08-29 16:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GatewayRouteDefinitionModify extends GatewayRouteDefinition {

    private String id;
}