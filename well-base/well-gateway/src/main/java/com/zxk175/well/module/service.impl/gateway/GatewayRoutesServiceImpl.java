package com.zxk175.well.module.service.impl.gateway;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxk175.well.base.consts.Const;
import com.zxk175.well.base.consts.enums.Deleted;
import com.zxk175.well.base.consts.enums.Enabled;
import com.zxk175.well.module.dao.gateway.GatewayRoutesDao;
import com.zxk175.well.module.entity.gateway.GatewayRoutes;
import com.zxk175.well.module.service.gateway.GatewayRoutesService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
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

    private RouteDefinitionWriter routeDefinitionWriter;
    private ApplicationEventPublisher publisher;


    @Override
    public String loadRouteDefinition() {
        try {
            List<GatewayRoutes> gatewayRoutes = this.listAll();
            if (CollUtil.isEmpty(gatewayRoutes)) {
                return "none route defined";
            }

            for (GatewayRoutes gatewayRoute : gatewayRoutes) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(gatewayRoute.getRouteId());
                definition.setUri(new URI(gatewayRoute.getRouteUri()));

                List<PredicateDefinition> predicateDefinitions = gatewayRoute.getPredicateDefinition();
                if (predicateDefinitions != null) {
                    definition.setPredicates(predicateDefinitions);
                }

                List<FilterDefinition> filterDefinitions = gatewayRoute.getFilterDefinition();
                if (filterDefinitions != null) {
                    definition.setFilters(filterDefinitions);
                }

                routeDefinitionWriter.save(Mono.just(definition)).subscribe();
                this.publisher.publishEvent(new RefreshRoutesEvent(this));
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

    @Override
    public List<GatewayRoutes> listAll() {
        QueryWrapper<GatewayRoutes> gatewayRoutesQw = new QueryWrapper<>();
        gatewayRoutesQw.eq(Const.DB_DELETED, Deleted.NO.value());
        gatewayRoutesQw.eq(Const.DB_ENABLED, Enabled.YES.value());

        return baseMapper.selectList(gatewayRoutesQw);
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
        gatewayRoutesQw.eq(Const.DB_DELETED, Deleted.NO.value());
        gatewayRoutesQw.eq("", id);
        return this.removeById(id);
    }
}
