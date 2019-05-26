package com.zxk175.well.config.shiro;

import com.google.common.collect.Maps;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.Map;

/**
 * @author zxk175
 */
@Configuration
public class MyShiroConfig {

    @Bean("securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(JwtRealm jwtRealm) {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        // 使用自己的realm
        webSecurityManager.setRealm(jwtRealm);
        webSecurityManager.setRememberMeManager(null);
        webSecurityManager.setSubjectFactory(new MySubjectFactory());

        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();

        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);

        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        webSecurityManager.setSubjectDAO(subjectDAO);

        return webSecurityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为statelessAuth
        String jwt = "jwt";
        String anon = "anon";
        Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
        filterMap.put(jwt, new JwtAuthFilter());

        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 自定义url规则
        // http://shiro.apache.org/web.html#urls
        Map<String, String> filterRule = Maps.newLinkedHashMap();

        // 所有请求通过StatelessAuthFilter
        filterRule.put("/**/in-data/**", anon);
        filterRule.put("/**/sys-*/**", jwt);

        // 访问其它页面不通过StatelessAuthFilter
        filterRule.put("/**", anon);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRule);

        return shiroFilterFactoryBean;
    }

    // 添加注解支持
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);

        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);

        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
        // 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
        // 加入这项配置能解决这个bug
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);

        return defaultAdvisorAutoProxyCreator;
    }
}

