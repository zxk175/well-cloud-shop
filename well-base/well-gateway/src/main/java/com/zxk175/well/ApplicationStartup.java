package com.zxk175.well;

import com.zxk175.well.base.consts.Const;
import com.zxk175.well.module.service.gateway.GatewayRoutesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author zxk175
 * @since 2019-08-30 13:01
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class ApplicationStartup implements ApplicationRunner {

    private GatewayRoutesService gatewayRoutesService;


    @Override
    public void run(ApplicationArguments args) {
        boolean flag = gatewayRoutesService.loadRouteDefinition();
        log.info("{}网关动态路由加载{}", Const.LOG_PREFIX, flag ? "成功" : "失败");
    }
}