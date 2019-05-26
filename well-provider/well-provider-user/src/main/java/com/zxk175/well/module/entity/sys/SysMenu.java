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
 * 系统菜单表
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:44
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_menu")
@ApiModel(value = "SysMenu对象", description = "系统菜单表")
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键", hidden = true)
    @TableId(value = "menu_id", type = IdType.ID_WORKER)
    private Long menuId;

    @ApiModelProperty(value = "父Id", example = "0")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "名称", example = "test")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "图标", example = "test")
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "路径", example = "test")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "授权标识", example = "test")
    @TableField("perms")
    private String perms;

    @ApiModelProperty(value = "类型 1目录 2菜单 3按钮", example = "0")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "排序", example = "0")
    @TableField("sort")
    private Integer sort;

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
        return this.menuId;
    }
}
