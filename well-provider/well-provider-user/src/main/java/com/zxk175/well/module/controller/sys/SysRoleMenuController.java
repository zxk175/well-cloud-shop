package com.zxk175.well.module.controller.sys;

import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.module.controller.BaseController;
import com.zxk175.well.module.entity.sys.SysRoleMenu;
import com.zxk175.well.module.service.sys.SysRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 角色与菜单关联表 前端控制器
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Controller
@AllArgsConstructor
@RequestMapping(Const.BASE_URL + "/sys-role-menu")
@Api(tags = "SysRoleMenu", description = "角色与菜单关联V1")
public class SysRoleMenuController extends BaseController {

    private SysRoleMenuService sysRoleMenuService;


    @ResponseBody
    @PostMapping(value = "/save/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加角色与菜单关联", notes = "添加角色与菜单关联")
    public Response save(@Validated @RequestBody SysRoleMenu sysRoleMenu) {
        boolean flag = sysRoleMenuService.save(sysRoleMenu);
        return saveReturn(flag);
    }
}
