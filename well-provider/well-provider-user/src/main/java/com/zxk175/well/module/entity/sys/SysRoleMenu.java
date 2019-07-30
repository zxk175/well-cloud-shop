package com.zxk175.well.module.entity.sys;

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
 * 角色与菜单关联表
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_role_menu")
@ApiModel(value = "SysRoleMenu对象", description = "角色与菜单关联表")
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键", hidden = true)
    @TableId(value = "rm_id", type = IdType.ID_WORKER)
    private Long rmId;

    @ApiModelProperty(value = "角色Id", example = "0")
    @TableField("role_id")
    private Long roleId;

    @ApiModelProperty(value = "菜单Id", example = "0")
    @TableField("menu_id")
    private Long menuId;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "状态", hidden = true)
    @TableField("state")
    private Integer state;


    @Override
    protected Serializable pkVal() {
        return this.rmId;
    }
}
