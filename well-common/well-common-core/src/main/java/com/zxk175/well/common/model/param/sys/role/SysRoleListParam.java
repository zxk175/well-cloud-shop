package com.zxk175.well.common.model.param.sys.role;

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
public class SysRoleListParam extends PageParam {

    @ApiModelProperty(value = "角色名", example = "超级")
    private String roleName;
}
