package com.zxk175.well.module.controller.gateway;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zxk175
 * @since 2019-09-03 09:13
 */
@Controller
@RequestMapping("/gateway-routes")
public class PageController {

    @GetMapping("/list")
    public String list() {
        return "gateway/list";
    }
}
