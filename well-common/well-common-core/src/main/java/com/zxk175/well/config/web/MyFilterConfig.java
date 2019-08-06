package com.zxk175.well.config.web;

import com.google.common.collect.Lists;
import com.zxk175.well.config.filter.RequestAuthFilter;
import com.zxk175.well.config.filter.request.RepeatedlyReadFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

/**
 * Filter配置
 * 根据order值的大小，从小到大的顺序依次过滤
 *
 * @author zxk175
 * @since 2019/03/23 15:00
 */
@Configuration
public class MyFilterConfig {

    @Bean
    public FilterRegistrationBean repeatedlyReadFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();

        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new RepeatedlyReadFilter());
        registration.setName("RepeatedlyReadFilter");
        registration.addUrlPatterns("/*");
        registration.setEnabled(true);
        registration.setOrder(1);

        return registration;
    }

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean<RequestAuthFilter> registration = new FilterRegistrationBean<>();

        registration.setDispatcherTypes(DispatcherType.REQUEST);
        // 注入过滤器
        registration.setFilter(new RequestAuthFilter());
        // 过滤器名称
        registration.setName("RequestAuthFilter");
        // 拦截规则
        List<String> paths = Lists.newArrayList();
        paths.add("/");
        paths.add("/v2/*");
        paths.add("/swagger-ui.html");
        paths.add("/swagger-resources");
        paths.add("/swagger-resources/configuration/ui");
        registration.setUrlPatterns(paths);
        // 是否自动注册 false 取消Filter的自动注册
        registration.setEnabled(true);
        // 过滤器顺序
        registration.setOrder(999998);

        return registration;
    }

    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        registration.setName("shiroFilter");
        // 该值缺省为false，表示生命周期由SpringApplicationContext管理
        // 设置为true，则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.addUrlPatterns("/*");
        registration.setEnabled(true);
        registration.setOrder(999999);

        return registration;
    }
}
