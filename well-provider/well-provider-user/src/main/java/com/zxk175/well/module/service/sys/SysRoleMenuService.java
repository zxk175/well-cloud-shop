package com.zxk175.well.module.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxk175.well.module.entity.sys.SysRoleMenu;

import java.util.List;

/**
 * <p>
 * 角色与菜单关联表 服务类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    void removeBatch(List<String> param);

    void saveOrModify(Long roleId, List<String> menuList);
}