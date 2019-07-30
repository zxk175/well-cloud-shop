package com.zxk175.well.module.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.sd4324530.jtuple.Tuple2;
import com.github.sd4324530.jtuple.Tuples;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.consts.enums.MenuType;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.menu.SysMenuRemoveParam;
import com.zxk175.well.common.util.MyStrUtil;
import com.zxk175.well.module.controller.BaseController;
import com.zxk175.well.module.entity.sys.SysMenu;
import com.zxk175.well.module.service.sys.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:44
 */
@Controller
@RequestMapping(Const.BASE_URL + "/sys-menu")
@Api(tags = "SysMenu", description = "系统菜单V1")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;


    @ResponseBody
    @PostMapping(value = "/save/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加系统菜单", notes = "添加系统菜单")
    @RequiresPermissions("sys:menu:save")
    public Response save(@RequestBody SysMenu menu) {
        Tuple2<Boolean, Response> tuple = verifyMenu(menu);
        if (tuple.first) {
            boolean save = sysMenuService.save(menu);
            return saveReturn(save);
        }

        return tuple.second;
    }

    @ResponseBody
    @PostMapping(value = "/remove/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "删除系统菜单", notes = "删除系统菜单")
    @RequiresPermissions("sys:menu:remove")
    public Response remove(@Validated @RequestBody SysMenuRemoveParam param) {
        return sysMenuService.removeMenu(param);
    }

    @ResponseBody
    @PostMapping(value = "/modify/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "修改系统菜单", notes = "修改系统菜单")
    @RequiresPermissions("sys:menu:modify")
    public Response modify(@RequestBody SysMenu menu) {
        Tuple2<Boolean, Response> tuple = verifyMenu(menu);
        if (tuple.first) {
            boolean modify = sysMenuService.updateById(menu);
            return modifyReturn(modify);
        }

        return tuple.second;
    }

    @ResponseBody
    @GetMapping(value = "/nav/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "左侧菜单列表", notes = "左侧菜单列表")
    public Response nav() {
        return sysMenuService.listUserMenu(getUser());
    }

    @ResponseBody
    @GetMapping(value = "/list/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "所有菜单列表", notes = "所有菜单列表")
    @RequiresPermissions("sys:menu:list")
    public Response list() {
        return sysMenuService.listMenu(false);
    }

    @ResponseBody
    @GetMapping(value = "/select/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "选择菜单(添加、修改菜单)", notes = "选择菜单(添加、修改菜单)")
    @RequiresPermissions("sys:menu:list")
    public Response select() {
        // 查询列表数据
        List<SysMenu> menuList = sysMenuService.listNotButtonList();

        // 添加顶级菜单
        SysMenu root = new SysMenu();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(0L);

        menuList.add(root);

        return collReturn(menuList);
    }

    @ResponseBody
    @GetMapping(value = "/info/{menuId}/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "系统菜单信息", notes = "系统菜单信息")
    @RequiresPermissions("sys:menu:info")
    public Response info(@PathVariable("menuId") Long menuId) {
        QueryWrapper<SysMenu> sysMenuQW = new QueryWrapper<>();
        sysMenuQW.select("menu_id, parent_id, name, icon, url, perms, type, sort, state");
        sysMenuQW.eq("menu_id", menuId);

        SysMenu menu = sysMenuService.getOne(sysMenuQW);
        return objectReturn(menu);
    }

    private Tuple2<Boolean, Response> verifyMenu(SysMenu menu) {
        if (MyStrUtil.isBlank(menu.getName())) {
            return Tuples.tuple(false, fail("菜单名称不能为空"));
        }

        if (menu.getParentId() == null) {
            return Tuples.tuple(false, fail("上级菜单不能为空"));
        }

        // 菜单
        if (menu.getType().equals(MenuType.MENU.value())) {
            if (MyStrUtil.isBlank(menu.getUrl())) {
                return Tuples.tuple(false, fail("菜单URL不能为空"));
            }
        }

        // 上级菜单类型
        int parentType = MenuType.CATALOG.value();
        if (menu.getParentId() != 0) {
            QueryWrapper<SysMenu> sysMenuQW = new QueryWrapper<>();
            sysMenuQW.select("menu_id, parent_id, name, icon, url, perms, type, sort, state");
            sysMenuQW.eq("menu_id", menu.getParentId());

            SysMenu parentMenu = sysMenuService.getOne(sysMenuQW);
            parentType = parentMenu.getType();
        }

        // 目录、菜单
        if (menu.getType().equals(MenuType.CATALOG.value()) ||
                menu.getType().equals(MenuType.MENU.value())) {
            if (parentType != MenuType.CATALOG.value()) {
                return Tuples.tuple(false, fail("上级菜单只能为目录类型"));
            }
        }

        // 按钮
        if (menu.getType().equals(MenuType.BUTTON.value())) {
            if (parentType != MenuType.MENU.value()) {
                return Tuples.tuple(false, fail("上级菜单只能为菜单类型"));
            }
        }

        return Tuples.tuple(true, ok());
    }
}
