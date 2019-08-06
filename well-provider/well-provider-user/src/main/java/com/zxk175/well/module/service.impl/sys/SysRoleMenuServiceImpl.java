package com.zxk175.well.module.service.impl.sys;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.module.dao.sys.SysRoleMenuDao;
import com.zxk175.well.module.entity.sys.SysRoleMenu;
import com.zxk175.well.module.service.sys.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色与菜单关联表 服务实现类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenu> implements SysRoleMenuService {

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void removeBatch(List<String> param) {
        QueryWrapper<SysRoleMenu> sysRoleMenuQw = new QueryWrapper<>();
        sysRoleMenuQw.in("role_id", param);

        this.remove(sysRoleMenuQw);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void saveOrModify(Long roleId, List<String> menuList) {
        // 先删除角色与菜单关联
        QueryWrapper<SysRoleMenu> sysRoleMenuQw = new QueryWrapper<>();
        sysRoleMenuQw.eq("role_id", roleId);
        this.remove(sysRoleMenuQw);

        List<SysRoleMenu> dataList = Lists.newArrayList();
        for (String menuId : menuList) {
            // 保存角色与菜单关联
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(Convert.toLong(menuId));

            dataList.add(sysRoleMenu);
        }

        this.saveBatch(dataList, Const.DB_BATCH_SIZE);
    }
}
