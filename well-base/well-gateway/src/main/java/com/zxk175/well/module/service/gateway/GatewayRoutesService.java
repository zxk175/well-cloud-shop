package com.zxk175.well.module.service.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxk175.well.module.entity.gateway.GatewayRoutes;

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

    String loadRouteDefinition();

    List<GatewayRoutes> listAll();

    boolean saveRoutes(GatewayRoutes gatewayRoutes);

    boolean modify(GatewayRoutes gatewayRoutes);

    boolean deleteById(String id);
}
