package com.zxk175.well;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zxk175
 * @since 2018/12/25 16:18
 */
@Controller
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
