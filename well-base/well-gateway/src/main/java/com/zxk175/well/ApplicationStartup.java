package com.zxk175.well;

import com.zxk175.well.module.service.gateway.GatewayRoutesService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author zxk175
 * @since 2019-08-30 13:01
 */
@Configuration
@AllArgsConstructor
public class ApplicationStartup implements ApplicationRunner {

    private GatewayRoutesService gatewayRoutesService;


    @Override
    public void run(ApplicationArguments args) {
        gatewayRoutesService.loadRouteDefinition();
    }
}