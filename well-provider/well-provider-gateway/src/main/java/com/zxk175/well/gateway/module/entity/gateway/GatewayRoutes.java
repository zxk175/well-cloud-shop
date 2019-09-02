package com.zxk175.well.gateway.module.entity.gateway;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 网关路由表
 * </p>
 *
 * @author zxk175
 * @since 2019-08-29 15:15:16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_gateway_routes")
@ApiModel(value = "GatewayRoutes对象", description = "网关路由表")
public class GatewayRoutes extends Model<GatewayRoutes> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "路由Id", example = "test")
    @TableField("route_id")
    private String routeId;

    @ApiModelProperty(value = "转发目标Uri", example = "test")
    @TableField("route_uri")
    private String routeUri;

    @ApiModelProperty(value = "路由执行顺序", example = "0")
    @TableField("route_order")
    private Integer routeOrder;

    @ApiModelProperty(value = "断言字符串集合", example = "test")
    @TableField("predicates")
    private String predicates;

    @ApiModelProperty(value = "过滤器字符串集合", example = "test")
    @TableField("filters")
    private String filters;

    @ApiModelProperty(value = "是否删除")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "是否启用")
    @TableField("enabled")
    private Integer enabled;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
