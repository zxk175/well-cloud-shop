package com.zxk175.well.base.consts.enums;

import com.zxk175.well.base.consts.Const;

/**
 * @author zxk175
 * @since 2019-08-29 16:02
 */
public enum Deleted {

    // 逻辑删除 0已删 1未删
    YES(Const.ZERO),
    NO(Const.ONE),

    ;

    private Integer value;

    Deleted(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }
}
