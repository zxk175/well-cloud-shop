package com.zxk175.well.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zxk175
 * @since 2019-08-11 02:43
 */
@Data
public class AddParam {

    @ApiModelProperty(value = "a", example = "10")
    private Integer a;

    @ApiModelProperty(value = "b", example = "10")
    private Integer b;
}
