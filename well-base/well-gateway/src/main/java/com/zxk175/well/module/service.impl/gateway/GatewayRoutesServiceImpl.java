package com.zxk175.well.module.service.impl.gateway;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.zxk175.well.base.util.MyStrUtil;
import com.zxk175.well.base.util.json.FastJsonUtil;
import com.zxk175.well.gateway.model.dto.GatewayRoutesDto;
import com.zxk175.well.module.service.gateway.GatewayRoutesService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 网关路由表 服务实现类
 * </p>
 *
 * @author zxk175
 * @since 2019-08-29 15:10:07
 */
@Service
@AllArgsConstructor
public class GatewayRoutesServiceImpl implements GatewayRoutesService {

    private ApplicationEventPublisher publisher;
    private RouteDefinitionWriter routeDefinitionWriter;
    private InMemoryRouteDefinitionRepository inMemoryRouteDefinitionRepository;


    @Override
    public boolean loadRouteDefinition() {
        try {
            List<GatewayRoutesDto> gatewayRoutes = new ArrayList<>();
            if (CollUtil.isEmpty(gatewayRoutes)) {
                return false;
            }

            for (GatewayRoutesDto gatewayRoute : gatewayRoutes) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(gatewayRoute.getRouteId());
                definition.setUri(URI.create(gatewayRoute.getRouteUri()));
                definition.setPredicates(getPredicates(gatewayRoute.getPredicates()));
                definition.setFilters(getFilters(gatewayRoute.getFilters()));

                routeDefinitionWriter.save(Mono.just(definition)).subscribe();
                this.publisher.publishEvent(new RefreshRoutesEvent(this));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<GatewayRoutesDto> listByMem() {
        List<GatewayRoutesDto> gatewayRoutes = new ArrayList<>();

        inMemoryRouteDefinitionRepository.getRouteDefinitions().subscribe(routeDefinition -> {
            GatewayRoutesDto gatewayRoute = new GatewayRoutesDto();
            gatewayRoute.setRouteId(routeDefinition.getId());
            gatewayRoute.setRouteUri(String.valueOf(routeDefinition.getUri()));
            gatewayRoute.setRouteOrder(routeDefinition.getOrder());
            gatewayRoute.setPredicates(FastJsonUtil.jsonStr(routeDefinition.getPredicates()));
            gatewayRoute.setFilters(FastJsonUtil.jsonStr(routeDefinition.getFilters()));

            gatewayRoutes.add(gatewayRoute);
        });

        return gatewayRoutes;
    }

    private List<PredicateDefinition> getPredicates(String predicates) {
        if (MyStrUtil.isBlank(predicates)) {
            return Collections.emptyList();
        }

        return JSON.parseArray(predicates, PredicateDefinition.class);
    }

    private List<FilterDefinition> getFilters(String filters) {
        if (MyStrUtil.isBlank(filters)) {
            return Collections.emptyList();
        }

        return JSON.parseArray(filters, FilterDefinition.class);
    }
}
