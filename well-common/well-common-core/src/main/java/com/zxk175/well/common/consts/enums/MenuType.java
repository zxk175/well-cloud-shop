package com.zxk175.well.common.consts.enums;

import com.zxk175.well.common.consts.Const;

/**
 * @author zxk175
 * @since 2019/03/28 10:38
 */
public enum MenuType {

    // 目录
    CATALOG(Const.ONE),
    // 菜单
    MENU(Const.TWO),
    // 按钮
    BUTTON(Const.THREE);


    private Integer value;

    MenuType(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }
}
