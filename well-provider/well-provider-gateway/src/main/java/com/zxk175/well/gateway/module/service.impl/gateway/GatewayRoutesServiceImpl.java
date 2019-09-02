package com.zxk175.well.gateway.module.service.impl.gateway;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxk175.well.base.consts.Const;
import com.zxk175.well.base.consts.enums.Deleted;
import com.zxk175.well.base.consts.enums.Enabled;
import com.zxk175.well.gateway.module.dao.gateway.GatewayRoutesDao;
import com.zxk175.well.gateway.module.entity.gateway.GatewayRoutes;
import com.zxk175.well.gateway.module.service.gateway.GatewayRoutesService;
import org.springframework.stereotype.Service;

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
public class GatewayRoutesServiceImpl extends ServiceImpl<GatewayRoutesDao, GatewayRoutes> implements GatewayRoutesService {

    @Override
    public List<GatewayRoutes> listByDb() {
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
