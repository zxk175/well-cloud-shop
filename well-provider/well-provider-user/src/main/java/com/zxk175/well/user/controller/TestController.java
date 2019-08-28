package com.zxk175.well.user.controller;

import com.zxk175.well.core.http.Response;
import com.zxk175.well.provider.bean.AddParam;
import com.zxk175.well.provider.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zxk175
 * @since 2019-08-28 11:09
 */
@Controller
@Api(tags = "测试")
public class TestController extends BaseController {

    @GetMapping("ex")
    public void exception() {
        throw new RuntimeException("触发测试异常");
    }


    @ResponseBody
    @PostMapping(value = "param-valid/v1")
    @ApiOperation(value = "测试参数校验v1", notes = "测试参数校验v1")
    public Response paramValidV1(@Validated @RequestBody AddParam param) {
        return ok(param.getA() + param.getB());
    }
}
