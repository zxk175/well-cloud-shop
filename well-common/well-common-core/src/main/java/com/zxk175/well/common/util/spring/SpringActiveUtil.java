package com.zxk175.well.common.util.spring;

import cn.hutool.core.util.ObjectUtil;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.MyStrUtil;
import com.zxk175.well.common.util.YmlUtil;

import java.util.Properties;

/**
 * @author zxk175
 * @since 2019/04/13 15:54
 */
public class SpringActiveUtil {

    public static String getActive() {
        String active;
        Properties properties = YmlUtil.yml2Properties("config/application.yml");
        if (ObjectUtil.isNull(properties)) {
            throw new RuntimeException("未找到config/application.yml");
        }

        active = properties.getProperty("spring.profiles.active", Const.TEST);
        if (MyStrUtil.isBlank(active)) {
            throw new RuntimeException("未获取到运行环境");
        }

        return active;
    }

    public static boolean getBoolean() {
        String active = getActive();

        return (MyStrUtil.eq(Const.DEV, active) || MyStrUtil.eq(Const.TEST, active));
    }

    public static String getChineseStr(boolean flag) {
        if (flag) {
            return "测试";
        }

        return "生产";
    }
}