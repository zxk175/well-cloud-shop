package com.zxk175.well.common.consts.enums;

/**
 * @author zxk175
 * @since 2019/03/28 10:38
 */
public enum ActiveType {

    // 开发
    DEV("开发", "dev"),
    // 测试
    TEST("测试", "test"),
    // 生产
    PROD("生产", "prod");


    private String view;
    private String value;

    ActiveType(String view, String value) {
        this.view = view;
        this.value = value;
    }

    public static ActiveType valueOfStr(String value) {
        for (ActiveType activeType : values()) {
            if (value.equals(activeType.value())) {
                return activeType;
            }
        }

        return ActiveType.DEV;
    }

    public String view() {
        return view;
    }

    public String value() {
        return value;
    }
}
