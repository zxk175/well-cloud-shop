package com.zxk175.well.module.controller.sys;

import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.module.entity.sys.SysMenu;
import com.zxk175.well.module.service.sys.SysMenuService;
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
import com.zxk175.well.module.controller.BaseController;

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
    public Response save(@Validated @RequestBody SysMenu sysMenu) {
        boolean flag = sysMenuService.save(sysMenu);
        return saveReturn(flag);
    }
}
