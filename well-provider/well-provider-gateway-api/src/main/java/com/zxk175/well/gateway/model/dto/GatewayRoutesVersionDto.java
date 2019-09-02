package com.zxk175.well.gateway.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zxk175
 * @since 2019-09-02 14:22
 */
@Data
public class GatewayRoutesVersionDto {

    @ApiModelProperty(value = "主键", hidden = true)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
