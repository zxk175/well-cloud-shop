package com.zxk175.well.module.controller.sys;

import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.module.entity.sys.SysUserToken;
import com.zxk175.well.module.service.sys.SysUserTokenService;
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
 * 系统用户Token表 前端控制器
 * </p>
 *
 * @author zxk175
 * @since 2019-05-27 01:11:35
 */
@Controller
@RequestMapping(Const.BASE_URL + "/sys-user-token")
@Api(tags = "SysUserToken", description = "系统用户TokenV1")
public class SysUserTokenController extends BaseController {

    @Autowired
    private SysUserTokenService sysUserTokenService;


    @ResponseBody
    @PostMapping(value = "/save/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加系统用户Token", notes = "添加系统用户Token")
    public Response save(@Validated @RequestBody SysUserToken sysUserToken) {
        boolean flag = sysUserTokenService.save(sysUserToken);
        return saveReturn(flag);
    }
}
