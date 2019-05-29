package com.zxk175.well.module.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxk175.well.common.model.param.sys.menu.SysMenuListParam;
import com.zxk175.well.module.entity.sys.SysMenu;

import java.util.List;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:44
 */
public interface SysMenuDao extends BaseMapper<SysMenu> {

    List<SysMenu> listByUserId(SysMenuListParam param);
}
