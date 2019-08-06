package com.zxk175.well;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author zxk175
 * @since 2018/12/25 16:18
 */
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class WellConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellConfigApplication.class, args);
    }
}
