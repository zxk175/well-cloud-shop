package com.zxk175.well.common.model.param.sys.user;

import com.zxk175.well.common.model.param.sys.SysUserBaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zxk175
 * @since 2019/03/23 20:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserRemoveParam extends SysUserBaseParam {

    @ApiModelProperty(value = "身份标识", example = "1")
    private Integer identity;
}
