package com.zxk175.well.test.controller;

import com.zxk175.well.bean.AddParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zxk175
 * @since 2019/03/06 20:19
 */
@RestController
@RequestMapping("/calc")
@Api(tags = "测试")
public class TestController {


    @PostMapping("/add")
    @ApiOperation(value = "计算+", notes = "加法")
    public Integer add(@RequestBody AddParam param) {
        return param.getA() + param.getB();
    }
}
