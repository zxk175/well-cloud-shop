package com.zxk175.well;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin2.server.internal.EnableZipkinServer;

/**
 * @author zxk175
 * @since 2018/12/25 16:18
 */
@EnableZipkinServer
@EnableEurekaClient
@SpringBootApplication
public class WellZipkinServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellZipkinServerApplication.class, args);
    }
}
