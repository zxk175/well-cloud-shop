package com.zxk175.well.base.consts.enums;

import com.zxk175.well.base.consts.Const;

/**
 * @author zxk175
 * @since 2019-08-29 16:02
 */
public enum Enabled {

    // 是否启用 0禁用 1启用
    NO(Const.ZERO),
    YES(Const.ONE),

    ;

    private Integer value;

    Enabled(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }
}
