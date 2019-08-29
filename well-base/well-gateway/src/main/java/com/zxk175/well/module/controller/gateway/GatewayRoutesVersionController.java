package com.zxk175.well.module.controller.gateway;

import com.zxk175.well.base.http.Response;
import com.zxk175.well.module.entity.gateway.GatewayRoutesVersion;
import com.zxk175.well.module.service.gateway.GatewayRoutesVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 网关路由版本表 前端控制器
 * </p>
 *
 * @author zxk175
 * @since 2019-08-29 15:25:34
 */
@Controller
@AllArgsConstructor
@RequestMapping("/gateway-routes-version")
@Api(tags = "GatewayRoutesVersion", description = "网关路由版本V1")
public class GatewayRoutesVersionController {

    private GatewayRoutesVersionService gatewayRoutesVersionService;


    @ResponseBody
    @PostMapping(value = "/save/v1")
    @ApiOperation(value = "添加网关路由版本", notes = "添加网关路由版本")
    public Response save(@Validated @RequestBody GatewayRoutesVersion gatewayRoutesVersion) {
        boolean flag = gatewayRoutesVersionService.save(gatewayRoutesVersion);
        return Response.saveReturn(flag);
    }
}
