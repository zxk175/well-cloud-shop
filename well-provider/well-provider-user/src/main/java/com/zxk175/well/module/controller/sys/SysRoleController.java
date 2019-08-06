package com.zxk175.well.module.controller.sys;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.HttpMsg;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.role.SysRoleListParam;
import com.zxk175.well.common.util.common.CommonUtil;
import com.zxk175.well.module.controller.BaseController;
import com.zxk175.well.module.entity.sys.SysRole;
import com.zxk175.well.module.entity.sys.SysRoleMenu;
import com.zxk175.well.module.service.sys.SysRoleMenuService;
import com.zxk175.well.module.service.sys.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import java.util.Map;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Controller
@AllArgsConstructor
@RequestMapping(Const.BASE_URL + "/sys-role")
@Api(tags = "SysRole", description = "系统角色V1")
public class SysRoleController extends BaseController {

    private SysRoleService sysRoleService;
    private SysRoleMenuService sysRoleMenuService;


    @ResponseBody
    @PostMapping(value = "/save/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加系统角色", notes = "添加系统角色")
    @RequiresPermissions("sys:role:save")
    public Response save(@Validated @RequestBody SysRole sysRole) {
        return sysRoleService.saveRole(sysRole);
    }

    @ResponseBody
    @PostMapping(value = "/remove/batch/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "批量删除系统角色", notes = "批量删除系统角色")
    @RequiresPermissions("sys:role:remove")
    public Response remove(@Validated @RequestBody List<String> param) {
        return sysRoleService.removeRoles(param);
    }

    @ResponseBody
    @PostMapping(value = "/modify/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "修改系统角色", notes = "修改系统角色")
    @RequiresPermissions("sys:role:modify")
    public Response modify(@RequestBody SysRole sysRole) {
        return sysRoleService.modifyRole(sysRole);
    }

    @ResponseBody
    @PostMapping(value = "/list/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "所有系统角色列表", notes = "所有系统角色列表")
    @RequiresPermissions("sys:role:list")
    public Response list(@Validated @RequestBody SysRoleListParam param) {
        CommonUtil.buildPageParam(param);

        List<Map<String, Object>> dataList = sysRoleService.listSysRole(param);
        if (CollUtil.isEmpty(dataList)) {
            return fail(HttpMsg.NO_DATA);
        }

        Long count = sysRoleService.countSysRole(param);

        return CommonUtil.putPageExtraFalse(dataList, count, param);
    }

    @ResponseBody
    @GetMapping(value = "/info/{roleId}/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "系统角色信息", notes = "系统角色信息")
    @RequiresPermissions("sys:role:info")
    public Response info(@PathVariable("roleId") String roleId) {
        QueryWrapper<SysRole> sysRoleQw = new QueryWrapper<>();
        sysRoleQw.select("role_id, role_name, role_sign, remark, state");
        sysRoleQw.eq("role_id", roleId);
        SysRole sysRole = sysRoleService.getOne(sysRoleQw);

        // 查询角色对应的菜单
        QueryWrapper<SysRoleMenu> sysRoleMenuQw = new QueryWrapper<>();
        sysRoleMenuQw.select("menu_id");
        sysRoleMenuQw.eq("role_id", roleId);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.list(sysRoleMenuQw);
        List<String> menuList = Lists.newArrayList();
        for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
            menuList.add(Convert.toStr(sysRoleMenu.getMenuId()));
        }

        sysRole.setMenuList(menuList);

        return objectReturn(sysRole);
    }
}
