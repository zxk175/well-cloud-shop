package com.zxk175.well;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zxk175
 * @since 2019-08-11 00:11
 */
@Slf4j
@Controller
@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication
public class WellGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellGatewayApplication.class, args);
    }

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }
}
