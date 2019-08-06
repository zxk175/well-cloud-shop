package com.zxk175.well;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zxk175
 * @since 2018/12/25 16:18
 */
@EnableEurekaServer
@SpringBootApplication
public class WellEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellEurekaApplication.class, args);
    }
}
