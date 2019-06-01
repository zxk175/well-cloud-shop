package com.zxk175.well.module.controller.index;

import com.zxk175.well.module.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zxk175
 * @since 2019/03/06 20:19
 */
@Controller
public class IndexController extends BaseController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "User");
        return "index";
    }
}
