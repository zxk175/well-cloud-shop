package com.zxk175.well.module.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.consts.enums.MenuType;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.menu.SysMenuListParam;
import com.zxk175.well.common.model.param.sys.menu.SysMenuRemoveParam;
import com.zxk175.well.common.util.jwt.bean.SysSubjectDTO;
import com.zxk175.well.module.dao.sys.SysMenuDao;
import com.zxk175.well.module.entity.sys.SysMenu;
import com.zxk175.well.module.service.sys.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:44
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {

    @Override
    public Response listMenu(boolean isTerm) {
        QueryWrapper<SysMenu> sysMenuQW = new QueryWrapper<>();
        sysMenuQW.select("menu_id, parent_id, name, icon, url, perms, type, sort, state");
        if (isTerm) {
            sysMenuQW.ne("type", MenuType.BUTTON.value());
            sysMenuQW.eq(Const.DB_STATE, Const.ONE);
        }
        sysMenuQW.orderByAsc(Const.DB_SORT);

        List<SysMenu> menuList = this.list(sysMenuQW);
        return treeMenu(menuList);
    }

    @Override
    public Response listUserMenu(SysSubjectDTO sysSubjectDTO) {
        if (sysSubjectDTO.hasSupper()) {
            return this.listMenu(true);
        }

        SysMenuListParam param = new SysMenuListParam();
        param.setUserId(String.valueOf(sysSubjectDTO.getUserId()));
        param.setMenuType(MenuType.BUTTON.value());

        List<SysMenu> menuList = baseMapper.listByUserId(param);
        return treeMenu(menuList);
    }

    @Override
    public Response removeMenu(SysMenuRemoveParam param) {
        Long menuId = Long.valueOf(param.getMenuId());
        // 判断是否有子菜单或按钮
        List<SysMenu> menuList = listMenuByParentId(menuId, true);
        if (menuList.size() > 0) {
            return Response.fail("请先删除子菜单或按钮");
        }

        Integer state = param.getState();
        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuId(menuId);
        sysMenu.setState(state);
        boolean remove = this.updateById(sysMenu);

        if (state.equals(Const.ZERO)) {
            return Response.removeReturn(remove);
        } else {
            return Response.diyReturn(remove, "恢复成功", "恢复失败");
        }
    }

    @Override
    public List<SysMenu> listMenuByParentId(Long parentId, boolean isRemove) {
        QueryWrapper<SysMenu> sysMenuQW = new QueryWrapper<>();
        sysMenuQW.select("menu_id, parent_id, name, icon, url, perms, type, sort, state");
        sysMenuQW.eq("parent_id", parentId);
        if (isRemove) {
            sysMenuQW.eq(Const.DB_STATE, Const.ONE);
        }
        sysMenuQW.orderByDesc(Const.DB_SORT);

        return baseMapper.selectList(sysMenuQW);
    }

    @Override
    public List<SysMenu> listNotButtonList() {
        QueryWrapper<SysMenu> sysMenuQW = new QueryWrapper<>();
        sysMenuQW.select("menu_id, parent_id, name, icon, url, perms, type, sort, state");
        sysMenuQW.ne("type", MenuType.BUTTON.value());
        sysMenuQW.orderByAsc(Const.DB_SORT);

        return baseMapper.selectList(sysMenuQW);
    }

    private Response treeMenu(List<SysMenu> menuList) {
        List<SysMenu> lastMenuList = Lists.newArrayList();
        // 先找到所有的1级菜单
        for (SysMenu menu : menuList) {
            // 1级菜单 parentId = 0
            if (menu.getParentId().equals(0L)) {
                lastMenuList.add(menu);
            }
        }

        // 为一级菜单设置子菜单，listChild递归调用
        for (SysMenu menu : lastMenuList) {
            menu.setChildren(listChild(menu, menuList));
        }

        return Response.collReturn(lastMenuList);
    }

    /**
     * 递归查找子菜单
     *
     * @param parentMenu 当前菜单Id
     * @param rootMenu   要查找的列表
     * @return ignore
     */
    private List<SysMenu> listChild(SysMenu parentMenu, List<SysMenu> rootMenu) {
        // 子菜单
        Long parentMenuId = parentMenu.getMenuId();
        List<SysMenu> childList = Lists.newArrayList();
        for (SysMenu menu : rootMenu) {
            // 遍历所有节点，父菜单Id大于0，则存在子菜单
            Long menuId = menu.getParentId();
            if (menuId > 0) {
                if (menuId.equals(parentMenuId)) {
                    childList.add(menu);
                }
            }
        }

        // 把子菜单的子菜单再循环一遍
        for (SysMenu menu : childList) {
            // 父菜单Id大于0，则存在子菜单
            if (menu.getParentId() > 0) {
                // 递归
                menu.setChildren(listChild(menu, rootMenu));
            }
        }

        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }

        return childList;
    }
}
