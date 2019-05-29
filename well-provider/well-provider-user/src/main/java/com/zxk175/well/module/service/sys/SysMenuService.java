package com.zxk175.well.module.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.menu.SysMenuRemoveParam;
import com.zxk175.well.common.util.jwt.bean.SysSubjectDTO;
import com.zxk175.well.module.entity.sys.SysMenu;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:44
 */
public interface SysMenuService extends IService<SysMenu> {

    Response listMenu(boolean isTerm);

    Response listUserMenu(SysSubjectDTO sysSubjectDTO);

    Response removeMenu(SysMenuRemoveParam param);

    List<SysMenu> listMenuByParentId(Long parentId, boolean isRemove);

    List<SysMenu> listNotButtonList();
}
