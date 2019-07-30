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
import java.util.List;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_user")
@ApiModel(value = "SysUser对象", description = "系统用户表")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键", hidden = true)
    @TableId(value = "user_id", type = IdType.ID_WORKER)
    private Long userId;

    @ApiModelProperty(value = "名字", example = "test")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "头像", example = "test")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "手机号", example = "test")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "盐值", example = "test")
    @TableField("salt")
    private String salt;

    @ApiModelProperty(value = "密码", example = "test")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "身份标识", example = "0")
    @TableField("identity")
    private Integer identity;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "状态", hidden = true)
    @TableField("state")
    private Integer state;

    @TableField(exist = false)
    private String roleName;

    @TableField(exist = false)
    private List<String> roleList;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }
}
