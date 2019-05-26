package com.zxk175.well.module.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_role")
@ApiModel(value = "SysRole对象", description = "系统角色表")
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "角色 Id", hidden = true)
    @TableId(value = "role_id", type = IdType.ID_WORKER)
    private Long roleId;

    @ApiModelProperty(value = "角色名称", example = "test")
    @TableField("role_name")
    private String roleName;

    @ApiModelProperty(value = "角色标识", example = "test")
    @TableField("role_sign")
    private String roleSign;

    @ApiModelProperty(value = "备注", example = "test")
    @TableField("remark")
    private String remark;

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
        return this.roleId;
    }
}
