package com.zxk175.well.common.model.param.sys.user;

import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.model.param.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zxk175
 * @since 2019/03/23 20:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserListParam extends PageParam {

    @ApiModelProperty(value = "用户Id", example = Const.DEFAULT_UID)
    private String userId;

    @ApiModelProperty(value = "手机号", example = Const.DEFAULT_MOBILE)
    private String mobile;
}
