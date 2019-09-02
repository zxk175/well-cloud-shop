package com.zxk175.well.gateway.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zxk175
 * @since 2019-08-29 16:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GatewayRouteDefinitionParamModify extends GatewayRouteDefinitionParamSave {

    private String id;
}