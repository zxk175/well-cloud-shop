package com.zxk175.well.provider.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zxk175
 * @since 2019-08-28 11:09
 */
@Data
public class AddParam {

    @NotNull
    @ApiModelProperty(value = "a", example = "10")
    private Integer a;

    @NotNull
    @ApiModelProperty(value = "b", example = "10")
    private Integer b;
}
