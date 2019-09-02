package com.zxk175.well.gateway.module.controller.gateway;

import cn.hutool.core.bean.BeanUtil;
import com.zxk175.well.base.http.Response;
import com.zxk175.well.base.util.json.FastJsonUtil;
import com.zxk175.well.gateway.model.param.GatewayRouteDefinitionParamModify;
import com.zxk175.well.gateway.model.param.GatewayRouteDefinitionParamSave;
import com.zxk175.well.gateway.module.entity.gateway.GatewayRoutes;
import com.zxk175.well.gateway.module.service.gateway.GatewayRoutesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 网关路由表 前端控制器
 * </p>
 *
 * @author zxk175
 * @since 2019-08-29 15:25:34
 */
@Controller
@AllArgsConstructor
@RequestMapping("/gateway-routes")
@Api(tags = "GatewayRoutes", description = "网关路由V1")
public class GatewayRoutesController {

    private GatewayRoutesService gatewayRoutesService;


    @ResponseBody
    @GetMapping(value = "/list-db/v1")
    @ApiOperation(value = "网关路由列表ByDb", notes = "网关路由列表ByDb")
    public Response listByDb() {
        List<GatewayRoutes> gatewayRoutes = gatewayRoutesService.listByDb();
        
        return Response.collReturn(gatewayRoutes);
    }

    @ResponseBody
    @PostMapping(value = "/save/v1")
    @ApiOperation(value = "添加网关路由", notes = "添加网关路由")
    public Response save(@Validated @RequestBody GatewayRouteDefinitionParamSave param) {
        GatewayRoutes gatewayRoutes = getGatewayRoutes(param);
        boolean flag = gatewayRoutesService.saveRoutes(gatewayRoutes);

        return Response.saveReturn(flag);
    }

    @ResponseBody
    @PostMapping(value = "/remove/{id}/v1")
    @ApiOperation(value = "删除网关路由", notes = "删除网关路由")
    public Response delete(@PathVariable String id) {
        boolean flag = gatewayRoutesService.deleteById(id);

        return Response.removeReturn(flag);
    }

    @ResponseBody
    @PostMapping(value = "/modify/v1")
    @ApiOperation(value = "修改网关路由", notes = "修改网关路由")
    public Response update(@Validated @RequestBody GatewayRouteDefinitionParamModify param) {
        GatewayRoutes gatewayRoutes = getGatewayRoutes(param);
        boolean flag = gatewayRoutesService.modify(gatewayRoutes);

        return Response.modifyReturn(flag);
    }

    private GatewayRoutes getGatewayRoutes(GatewayRouteDefinitionParamSave param) {
        GatewayRoutes gatewayRoutes = new GatewayRoutes();
        BeanUtil.copyProperties(param, gatewayRoutes);
        gatewayRoutes.setPredicates(FastJsonUtil.jsonStr(param.getPredicates()));
        gatewayRoutes.setFilters(FastJsonUtil.jsonStr(param.getFilters()));
        return gatewayRoutes;
    }
}
