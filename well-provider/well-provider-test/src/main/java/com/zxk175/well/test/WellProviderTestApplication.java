package com.zxk175.well.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zxk175
 * @since 2018/12/25 16:18
 */
@Controller
@EnableDiscoveryClient
@SpringBootApplication
public class WellProviderTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellProviderTestApplication.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "redirect:/swagger-ui.html";
    }
}
