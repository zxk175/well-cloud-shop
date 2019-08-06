package com.zxk175.well.common.util.spring;

import cn.hutool.core.util.ArrayUtil;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.consts.enums.ActiveType;
import com.zxk175.well.common.util.MyStrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author zxk175
 * @since 2019/04/13 15:54
 */
@Slf4j
@Component
@AllArgsConstructor
public class SpringActiveUtil {

    private Environment environment;
    private static SpringActiveUtil springActiveUtil;


    @PostConstruct
    public void init() {
        springActiveUtil = this;
    }

    public static String getActive() {
        return getActive(springActiveUtil.environment);
    }

    private static String getActive(Environment environment) {
        String[] active = environment.getActiveProfiles();
        if (ArrayUtil.isEmpty(active)) {
            active = environment.getDefaultProfiles();
        }

        if (ArrayUtil.isEmpty(active)) {
            throw new RuntimeException("未获取到运行环境");
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < 6; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            log.info("---> Index：{}，LineNum：{}，TraceInfo：{}", i, stackTraceElement.getLineNumber(), stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
        }

        return active[0];
    }

    public static boolean isDebug(Environment environment) {
        String active = getActive(environment);

        return isDebug(active);
    }

    public static boolean isDebug() {
        String active = getActive();

        return isDebug(active);
    }

    public static String getChineseStr() {
        String active = getActive();

        return ActiveType.valueOf(active).view();
    }

    private static boolean isDebug(String active) {
        return (MyStrUtil.eq(Const.DEV, active) || MyStrUtil.eq(Const.TEST, active));
    }
}