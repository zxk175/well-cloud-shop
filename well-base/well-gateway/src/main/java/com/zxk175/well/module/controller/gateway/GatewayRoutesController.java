package com.zxk175.well.module.controller.gateway;

import com.zxk175.well.base.http.Response;
import com.zxk175.well.gateway.model.GatewayFilterDefinition;
import com.zxk175.well.gateway.model.GatewayPredicateDefinition;
import com.zxk175.well.gateway.model.dto.GatewayRoutesDto;
import com.zxk175.well.gateway.model.param.GatewayRouteDefinitionParamModify;
import com.zxk175.well.gateway.model.param.GatewayRouteDefinitionParamSave;
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
    @ApiOperation(value = "网关路由列表ByMem", notes = "网关路由列表ByMem")
    public Response listByMem() {
        List<GatewayRoutesDto> gatewayRoutes = gatewayRoutesService.listByMem();

        return Response.collReturn(gatewayRoutes);
    }

    @ResponseBody
    @GetMapping(value = "/list-db/v1")
    @ApiOperation(value = "网关路由列表ByDb", notes = "网关路由列表ByDb")
    public Response listByDb() {
        List<GatewayRoutesDto> gatewayRoutes = new ArrayList<>();

        return Response.collReturn(gatewayRoutes);
    }

    @ResponseBody
    @PostMapping(value = "/save/v1")
    @ApiOperation(value = "添加网关路由", notes = "添加网关路由")
    public Response save(@Validated @RequestBody GatewayRouteDefinitionParamSave param) {
        RouteDefinition routeDefinition = assembleRouteDefinition(param);
        boolean flag = dynamicRouteService.save(routeDefinition);

        return Response.saveReturn(flag);
    }

    @ResponseBody
    @PostMapping(value = "/remove/{id}/v1")
    @ApiOperation(value = "删除网关路由", notes = "删除网关路由")
    public Response delete(@PathVariable String id) {
        boolean flag = dynamicRouteService.remove(id);

        return Response.removeReturn(flag);
    }

    @ResponseBody
    @PostMapping(value = "/modify/v1")
    @ApiOperation(value = "修改网关路由", notes = "修改网关路由")
    public Response update(@Validated @RequestBody GatewayRouteDefinitionParamModify param) {
        RouteDefinition routeDefinition = assembleRouteDefinition(param);
        boolean flag = dynamicRouteService.modify(routeDefinition);

        return Response.modifyReturn(flag);
    }

    private RouteDefinition assembleRouteDefinition(GatewayRouteDefinitionParamSave gatewayRoutes) {
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
