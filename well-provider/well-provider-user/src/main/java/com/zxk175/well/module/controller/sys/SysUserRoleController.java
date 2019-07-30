package com.zxk175.well.module.controller.sys;

import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.module.controller.BaseController;
import com.zxk175.well.module.entity.sys.SysUserRole;
import com.zxk175.well.module.service.sys.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 用户与角色关联表 前端控制器
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Controller
@RequestMapping(Const.BASE_URL + "/sys-user-role")
@Api(tags = "SysUserRole", description = "用户与角色关联V1")
public class SysUserRoleController extends BaseController {

    @Autowired
    private SysUserRoleService sysUserRoleService;


    @ResponseBody
    @PostMapping(value = "/save/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加用户与角色关联", notes = "添加用户与角色关联")
    public Response save(@Validated @RequestBody SysUserRole sysUserRole) {
        boolean flag = sysUserRoleService.save(sysUserRole);
        return saveReturn(flag);
    }
}
