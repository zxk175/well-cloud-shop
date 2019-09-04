package com.zxk175.well.module.service.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxk175.well.base.util.tuple.Tuple2;
import com.zxk175.well.module.entity.gateway.GatewayRoutes;
import com.zxk175.well.module.model.param.GatewayRouteDefinitionParamInfo;
import com.zxk175.well.module.model.param.GatewayRouteDefinitionParamList;

import java.util.List;

/**
 * <p>
 * 网关路由表 服务类
 * </p>
 *
 * @author zxk175
 * @since 2019-08-29 15:10:07
 */
public interface GatewayRoutesService extends IService<GatewayRoutes> {

    boolean loadRouteDefinition();

    List<GatewayRoutes> listByMem();

    Tuple2<List<GatewayRoutes>, Long> listByDb(GatewayRouteDefinitionParamList param);

    GatewayRoutes info(GatewayRouteDefinitionParamInfo param);

    boolean saveRoutes(GatewayRoutes gatewayRoutes);

    boolean modify(GatewayRoutes gatewayRoutes);

    boolean deleteById(String id);
}
