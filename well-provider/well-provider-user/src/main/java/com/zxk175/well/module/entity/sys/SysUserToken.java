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
 * 系统用户Token表
 * </p>
 *
 * @author zxk175
 * @since 2019-05-27 01:11:35
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_user_token")
@ApiModel(value = "SysUserToken对象", description = "系统用户Token表")
public class SysUserToken extends Model<SysUserToken> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键", hidden = true)
    @TableId(value = "token_id", type = IdType.ID_WORKER)
    private Long tokenId;

    @ApiModelProperty(value = "用户Id", example = "0")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "token", example = "test")
    @TableField("token")
    private String token;

    @ApiModelProperty(value = "过期时间", example = "test")
    @TableField("expire_time")
    private String expireTime;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.tokenId;
    }
}
