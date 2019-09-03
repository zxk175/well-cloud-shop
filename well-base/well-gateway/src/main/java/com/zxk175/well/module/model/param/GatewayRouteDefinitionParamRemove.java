package com.zxk175.well.module.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zxk175
 * @since 2019-08-29 16:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GatewayRouteDefinitionParamRemove extends GatewayRouteDefinitionParamInfo {

    private String routeId;
}