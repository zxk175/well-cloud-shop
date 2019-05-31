package com.zxk175.well.common.consts.enums;

import com.zxk175.well.common.consts.Const;

/**
 * @author zxk175
 * @since 2019/04/15 13:27
 */
public enum IdentityType {

    // 普通管理员
    ORDINARY(Const.ONE),
    // 超级管理员
    SUPER(Const.TWO);


    private Integer value;

    IdentityType(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }
}
