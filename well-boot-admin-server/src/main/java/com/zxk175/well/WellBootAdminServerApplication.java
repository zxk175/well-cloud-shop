package com.zxk175.well;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zxk175
 * @since 2018/12/25 16:18
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class WellBootAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellBootAdminServerApplication.class, args);
    }
}
