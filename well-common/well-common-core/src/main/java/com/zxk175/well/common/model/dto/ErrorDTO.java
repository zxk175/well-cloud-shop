package com.zxk175.well.common.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zxk175
 * @since 2019/03/23 19:15
 */
@Data
@Accessors(chain = true)
public class ErrorDTO implements Serializable {

    private String field;

    private String message;

    private Object rejectedValue;

    @JSONField(serialize = false)
    private Integer index;
}
