package com.zxk175.well.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author zxk175
 * @since 2019/05/26 13:39
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 2.1版本的security默认加上了 csrf 拦截, 所以需要通过重写方法, 把csrf拦截禁用
     * https://github.com/yinjihuan/spring-cloud/issues/1
     *
     * <pre>
     *     This is because @EnableWebSecurity is now added by default when Spring Security is on the classpath.
     *     This enable CSRF protection by default. You will have the same problem in 1.5.10 if you add @EnableWebSecurity.
     *     One work around, which is not the most secure workaround if you have browsers using the Eureka dashboard, is to disable CSRF protection.
     *     This can be done by adding the following configurations to your app.
     * </pre>
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();

        super.configure(httpSecurity);
    }
}