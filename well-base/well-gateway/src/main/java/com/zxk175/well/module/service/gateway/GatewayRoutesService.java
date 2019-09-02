package com.zxk175.well.module.service.gateway;

import com.zxk175.well.gateway.model.dto.GatewayRoutesDto;

import java.util.List;

/**
 * <p>
 * 网关路由表 服务类
 * </p>
 *
 * @author zxk175
 * @since 2019-08-29 15:10:07
 */
public interface GatewayRoutesService {

    boolean loadRouteDefinition();

    List<GatewayRoutesDto> listByMem();
}
