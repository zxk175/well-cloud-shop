package com.zxk175.well;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zxk175
 * @since 2018/12/25 16:18
 */
@EnableDiscoveryClient
@SpringBootApplication
public class WellProviderTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellProviderTestApplication.class, args);
    }
}
