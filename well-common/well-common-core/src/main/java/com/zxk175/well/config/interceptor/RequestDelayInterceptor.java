package com.zxk175.well.config.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.zxk175.well.common.annotation.request.RequestDelay;
import com.zxk175.well.common.util.id.ClockUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 限制请求频率拦截器
 * <p>
 * https://blog.csdn.net/u011704663/article/details/79304634
 */
public class RequestDelayInterceptor implements HandlerInterceptor {

    private long lastTime = 0;

    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 开始进入请求地址拦截
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RequestDelay delay = method.getAnnotation(RequestDelay.class);
            if (ObjectUtil.isNotNull(delay)) {
                return startDelay(delay.time());
            }
        }

        return true;
    }

    private boolean startDelay(int time) {
        long currentTime = ClockUtil.now();
        if (currentTime - lastTime > time) {
            lastTime = currentTime;
            return true;
        }

        return false;
    }
}

