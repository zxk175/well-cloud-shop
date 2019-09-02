package com.zxk175.well.gateway.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zxk175
 * @since 2019-09-02 14:21
 */
@Data
public class GatewayRoutesDto {

    @ApiModelProperty(value = "主键", hidden = true)
    private Long id;

    @ApiModelProperty(value = "路由Id", example = "test")
    private String routeId;

    @ApiModelProperty(value = "转发目标Uri", example = "test")
    private String routeUri;

    @ApiModelProperty(value = "路由执行顺序", example = "0")
    private Integer routeOrder;

    @JSONField(jsonDirect = true)
    @ApiModelProperty(value = "断言字符串集合", example = "test")
    private String predicates;

    @JSONField(jsonDirect = true)
    @ApiModelProperty(value = "过滤器字符串集合", example = "test")
    private String filters;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;

    @ApiModelProperty(value = "是否启用")
    private Integer enabled;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
}
