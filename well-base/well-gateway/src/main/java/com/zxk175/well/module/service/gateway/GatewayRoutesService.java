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

    /**
     * 加载路由
     *
     * @return ignore
     */
    boolean loadRouteDefinition();

    /**
     * 从内存中获取路由
     *
     * @return ignore
     */
    List<GatewayRoutes> listByMem();

    /**
     * 从数据库中获取路由
     *
     * @param param ignore
     * @return ignore
     */
    Tuple2<List<GatewayRoutes>, Long> listByDb(GatewayRouteDefinitionParamList param);

    /**
     * 路由信息
     *
     * @param param ignore
     * @return ignore
     */
    GatewayRoutes info(GatewayRouteDefinitionParamInfo param);

    /**
     * 添加路由
     *
     * @param gatewayRoutes ignore
     * @return ignore
     */
    boolean saveRoutes(GatewayRoutes gatewayRoutes);

    /**
     * 修改路由
     *
     * @param gatewayRoutes ignore
     * @return ignore
     */
    boolean modify(GatewayRoutes gatewayRoutes);

    /**
     * 删除路由
     *
     * @param id ignore
     * @return ignore
     */
    boolean deleteById(String id);
}
