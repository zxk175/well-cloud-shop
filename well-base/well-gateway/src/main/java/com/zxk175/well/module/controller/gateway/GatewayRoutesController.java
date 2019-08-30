package com.zxk175.well.module.controller.gateway;

import cn.hutool.core.bean.BeanUtil;
import com.zxk175.well.base.http.Response;
import com.zxk175.well.base.util.json.FastJsonUtil;
import com.zxk175.well.module.entity.gateway.GatewayRoutes;
import com.zxk175.well.module.model.GatewayFilterDefinition;
import com.zxk175.well.module.model.GatewayPredicateDefinition;
import com.zxk175.well.module.model.GatewayRouteDefinition;
import com.zxk175.well.module.model.GatewayRouteDefinitionModify;
import com.zxk175.well.module.service.gateway.DynamicRouteService;
import com.zxk175.well.module.service.gateway.GatewayRoutesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.util.ArrayList;
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

    private DynamicRouteService dynamicRouteService;
    private GatewayRoutesService gatewayRoutesService;


    @ResponseBody
    @GetMapping(value = "/list/v1")
    @ApiOperation(value = "网关路由列表", notes = "网关路由列表")
    public Response list() {
        List<GatewayRoutes> gatewayRoutes = gatewayRoutesService.list();
        return Response.collReturn(gatewayRoutes);
    }

    @ResponseBody
    @GetMapping(value = "/list-db/v1")
    @ApiOperation(value = "网关路由列表byDb", notes = "网关路由列表byDb")
    public Response listByDb() {
        List<GatewayRoutes> gatewayRoutes = gatewayRoutesService.list();
        return Response.collReturn(gatewayRoutes);
    }

    @ResponseBody
    @PostMapping(value = "/save/v1")
    @ApiOperation(value = "添加网关路由", notes = "添加网关路由")
    public Response save(@Validated @RequestBody GatewayRouteDefinition param) {
        RouteDefinition routeDefinition = assembleRouteDefinition(param);
        boolean flag = dynamicRouteService.save(routeDefinition);
        if (flag) {
            GatewayRoutes gatewayRoutes = getGatewayRoutes(param);
            flag = gatewayRoutesService.saveRoutes(gatewayRoutes);
        }

        return Response.saveReturn(flag);
    }

    @ResponseBody
    @PostMapping(value = "/remove/{id}/v1")
    @ApiOperation(value = "删除网关路由", notes = "删除网关路由")
    public Response delete(@PathVariable String id) {
        boolean flag = dynamicRouteService.remove(id);
        if (flag) {
            flag = gatewayRoutesService.deleteById(id);
        }

        return Response.removeReturn(flag);
    }

    @ResponseBody
    @PostMapping(value = "/modify/v1")
    @ApiOperation(value = "修改网关路由", notes = "修改网关路由")
    public Response update(@Validated @RequestBody GatewayRouteDefinitionModify param) {
        RouteDefinition routeDefinition = assembleRouteDefinition(param);
        boolean flag = dynamicRouteService.modify(routeDefinition);
        if (flag) {
            GatewayRoutes gatewayRoutes = getGatewayRoutes(param);
            flag = gatewayRoutesService.modify(gatewayRoutes);
        }

        return Response.modifyReturn(flag);
    }

    private GatewayRoutes getGatewayRoutes(GatewayRouteDefinition param) {
        GatewayRoutes gatewayRoutes = new GatewayRoutes();
        BeanUtil.copyProperties(param, gatewayRoutes);
        gatewayRoutes.setPredicates(FastJsonUtil.jsonStr(param.getPredicates()));
        gatewayRoutes.setFilters(FastJsonUtil.jsonStr(param.getFilters()));
        return gatewayRoutes;
    }

    private RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gatewayRoutes) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(gatewayRoutes.getRouteId());
        routeDefinition.setUri(URI.create(gatewayRoutes.getRouteUri()));

        // Predicates
        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        for (GatewayPredicateDefinition predicateDefinition : gatewayRoutes.getPredicates()) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(predicateDefinition.getArgs());
            predicate.setName(predicateDefinition.getName());
            predicateDefinitions.add(predicate);
        }

        routeDefinition.setPredicates(predicateDefinitions);

        // Filters
        List<FilterDefinition> filterDefinitions = new ArrayList<>();
        for (GatewayFilterDefinition filterDefinition : gatewayRoutes.getFilters()) {
            FilterDefinition filter = new FilterDefinition();
            filter.setArgs(filterDefinition.getArgs());
            filter.setName(filterDefinition.getName());
            filterDefinitions.add(filter);
        }

        routeDefinition.setFilters(filterDefinitions);

        return routeDefinition;
    }
}
