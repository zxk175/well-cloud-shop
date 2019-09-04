package com.zxk175.well.base.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zxk175
 * @since 2019-09-03 16:10
 */
@Data
@Accessors(chain = true)
public class BasePageParam implements Serializable {

    @NotNull
    @ApiModelProperty(value = "第几页", example = "0")
    private Long page;

    @ApiModelProperty(value = "每页记录数", example = "10")
    private Long size;
}
