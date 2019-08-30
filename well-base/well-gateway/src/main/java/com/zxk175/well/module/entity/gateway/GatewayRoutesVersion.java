package com.zxk175.well.module.entity.gateway;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 网关路由版本表
 * </p>
 *
 * @author zxk175
 * @since 2019-08-29 15:15:16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_gateway_routes_version")
@ApiModel(value = "GatewayRoutesVersion对象", description = "网关路由版本表")
public class GatewayRoutesVersion extends Model<GatewayRoutesVersion> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键", hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
