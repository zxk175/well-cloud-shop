package com.zxk175.well.user;

import com.zxk175.well.base.consts.Const;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zxk175
 * @since 2019-08-28 11:09
 */
@Controller
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.zxk175.well.user", Const.SCAN_PROVIDER, Const.SCAN_CORE})
public class WellProviderUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellProviderUserApplication.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "redirect:/swagger-ui.html";
    }
}
