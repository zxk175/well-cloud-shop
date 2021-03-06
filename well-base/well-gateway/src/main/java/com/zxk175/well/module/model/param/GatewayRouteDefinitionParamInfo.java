package com.zxk175.well.module.model.param;

import com.zxk175.well.base.check.NotBlank;
import lombok.Data;

/**
 * @author zxk175
 * @since 2019-08-29 16:41
 */
@Data
public class GatewayRouteDefinitionParamInfo {

    @NotBlank
    private String id;
}