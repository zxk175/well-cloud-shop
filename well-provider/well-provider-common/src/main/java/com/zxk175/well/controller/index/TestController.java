package com.zxk175.well.controller.index;

import com.google.common.collect.Maps;
import com.zxk175.well.common.check.Mobile;
import com.zxk175.well.common.check.NotBlank;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.SysUserBaseParam;
import com.zxk175.well.module.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zxk175
 * @since 2019/03/06 20:19
 */
@Validated
@Controller
@Api(tags = "Test", description = "测试请求V1")
public class TestController extends BaseController {

    @GetMapping("ex")
    public void exception() {
        throw new RuntimeException("触发测试异常");
    }

    @ResponseBody
    @PostMapping(value = "param-valid/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "测试参数校验v1", notes = "测试参数校验v1")
    public Response paramValidV1(@Validated @RequestBody SysUserBaseParam param) {
        return ok(param);
    }

    @ResponseBody
    @GetMapping(value = "param-valid/v2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "测试参数校验v2", notes = "测试参数校验v2")
    public Response paramValidV2(@Mobile @RequestParam("mobile") String mobile, @NotBlank(message = "code不能为空") @RequestParam("code") String code) {
        Map<Object, Object> data = Maps.newHashMap();
        data.put("mobile", mobile);
        data.put("code", code);

        return ok(data);
    }
}
