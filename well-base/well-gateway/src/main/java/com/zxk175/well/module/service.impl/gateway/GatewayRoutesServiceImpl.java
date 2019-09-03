package com.zxk175.well.module.service.impl.gateway;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxk175.well.base.consts.Const;
import com.zxk175.well.base.consts.enums.Enabled;
import com.zxk175.well.base.util.json.FastJsonUtil;
import com.zxk175.well.module.dao.gateway.GatewayRoutesDao;
import com.zxk175.well.module.entity.gateway.GatewayRoutes;
import com.zxk175.well.module.model.param.GatewayRouteDefinitionParamInfo;
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
public class GatewayRoutesServiceImpl extends ServiceImpl<GatewayRoutesDao, GatewayRoutes> implements GatewayRoutesService {

    private ApplicationEventPublisher publisher;
    private RouteDefinitionWriter routeDefinitionWriter;
    private InMemoryRouteDefinitionRepository inMemoryRouteDefinitionRepository;


    @Override
    public boolean loadRouteDefinition() {
        try {
            List<GatewayRoutes> gatewayRoutes = this.list();
            if (CollUtil.isEmpty(gatewayRoutes)) {
                return false;
            }

            for (GatewayRoutes gatewayRoute : gatewayRoutes) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(gatewayRoute.getRouteId());
                definition.setUri(URI.create(gatewayRoute.getRouteUri()));

                List<PredicateDefinition> predicateDefinitions = gatewayRoute.getPredicateDefinition();
                definition.setPredicates(CollUtil.isEmpty(predicateDefinitions) ? Collections.emptyList() : predicateDefinitions);

                List<FilterDefinition> filterDefinitions = gatewayRoute.getFilterDefinition();
                definition.setFilters(CollUtil.isEmpty(filterDefinitions) ? Collections.emptyList() : filterDefinitions);

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
    public List<GatewayRoutes> listByMem() {
        List<GatewayRoutes> gatewayRoutes = new ArrayList<>();

        inMemoryRouteDefinitionRepository.getRouteDefinitions().subscribe(routeDefinition -> {
            GatewayRoutes gatewayRoute = new GatewayRoutes();
            gatewayRoute.setRouteId(routeDefinition.getId());
            gatewayRoute.setRouteUri(String.valueOf(routeDefinition.getUri()));
            gatewayRoute.setRouteOrder(routeDefinition.getOrder());
            gatewayRoute.setPredicates(FastJsonUtil.jsonStr(routeDefinition.getPredicates()));
            gatewayRoute.setFilters(FastJsonUtil.jsonStr(routeDefinition.getFilters()));

            gatewayRoutes.add(gatewayRoute);
        });

        return gatewayRoutes;
    }

    @Override
    public GatewayRoutes info(GatewayRouteDefinitionParamInfo param) {
        QueryWrapper<GatewayRoutes> gatewayRoutesQw = new QueryWrapper<>();
        gatewayRoutesQw.eq(Const.DB_ENABLED, Enabled.YES.value());
        gatewayRoutesQw.eq(Const.DB_ID, param.getId());

        return this.getOne(gatewayRoutesQw);
    }

    @Override
    public List<GatewayRoutes> listByDb() {
        QueryWrapper<GatewayRoutes> gatewayRoutesQw = new QueryWrapper<>();
        gatewayRoutesQw.eq(Const.DB_ENABLED, Enabled.YES.value());

        return this.list(gatewayRoutesQw);
    }

    @Override
    public boolean modify(GatewayRoutes gatewayRoutes) {
        return this.updateById(gatewayRoutes);
    }

    @Override
    public boolean saveRoutes(GatewayRoutes gatewayRoutes) {
        return this.save(gatewayRoutes);
    }

    @Override
    public boolean deleteById(String id) {
        QueryWrapper<GatewayRoutes> gatewayRoutesQw = new QueryWrapper<>();
        gatewayRoutesQw.eq(Const.DB_ID, id);

        return this.removeById(id);
    }
}
