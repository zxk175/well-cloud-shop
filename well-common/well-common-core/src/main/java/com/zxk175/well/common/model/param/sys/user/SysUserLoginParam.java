package com.zxk175.well.common.model.param.sys.user;

import com.zxk175.well.common.check.Mobile;
import com.zxk175.well.common.consts.Const;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zxk175
 * @since 2019/04/01 15:56
 */
@Data
public class SysUserLoginParam {

    @Mobile
    @ApiModelProperty(value = "手机号", example = Const.DEFAULT_MOBILE)
    private String mobile;

    @ApiModelProperty(value = "密码", example = "123456")
    private String password;
}
