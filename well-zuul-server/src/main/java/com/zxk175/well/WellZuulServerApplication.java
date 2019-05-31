package com.zxk175.well;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author zxk175
 * @since 2018/12/25 16:18
 */
@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient
public class WellZuulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellZuulServerApplication.class, args);
    }
}
