package com.zxk175.well.module.controller.sys;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.HttpMsg;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.user.SysUserListParam;
import com.zxk175.well.common.model.param.sys.user.SysUserRemoveParam;
import com.zxk175.well.common.util.common.CommonUtil;
import com.zxk175.well.common.util.jwt.bean.SysSubjectDTO;
import com.zxk175.well.module.controller.BaseController;
import com.zxk175.well.module.entity.sys.SysUser;
import com.zxk175.well.module.entity.sys.SysUserRole;
import com.zxk175.well.module.service.sys.SysUserRoleService;
import com.zxk175.well.module.service.sys.SysUserService;
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
import java.util.Map;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Controller
@RequestMapping(Const.BASE_URL + "/sys-user")
@Api(tags = "SysUser", description = "系统用户V1")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;


    @ResponseBody
    @PostMapping(value = "/save/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加系统用户", notes = "添加系统用户")
    @RequiresPermissions("sys:user:save")
    public Response save(@Validated @RequestBody SysUser sysUser) {
        return sysUserService.saveUser(sysUser);
    }

    @ResponseBody
    @PostMapping(value = "/remove/batch/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "批量删除系统用户", notes = "批量删除系统用户")
    @RequiresPermissions("sys:user:remove")
    public Response remove(@Validated @RequestBody List<SysUserRemoveParam> param) {
        List<String> userIdList = Lists.newArrayList();
        for (SysUserRemoveParam removeParam : param) {
            if (CommonUtil.hasSupper(removeParam.getIdentity())) {
                return fail("超级管理员不能删除");
            }

            userIdList.add(removeParam.getUserId());
        }

        SysSubjectDTO user = getUser();
        String currentUserId = Convert.toStr(user.getUserId());
        if (CollUtil.contains(userIdList, currentUserId)) {
            return fail("当前用户不能删除");
        }

        return sysUserService.removeUsers(userIdList);
    }

    @ResponseBody
    @PostMapping(value = "/modify/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "修改系统用户", notes = "修改系统用户")
    @RequiresPermissions("sys:user:modify")
    public Response modify(@RequestBody SysUser sysUser) {
        return sysUserService.modifyUser(sysUser);
    }

    @ResponseBody
    @PostMapping(value = "/list/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "所有用户列表", notes = "所有用户列表")
    @RequiresPermissions("sys:user:list")
    public Response list(@Validated @RequestBody SysUserListParam param) {
        CommonUtil.buildPageParam(param);

        SysSubjectDTO sysSubjectDTO = getUser();
        boolean hasOrdinary = sysSubjectDTO.hasOrdinary();
        if (hasOrdinary) {
            param.setUserId(sysSubjectDTO.getUserIdStr());
        }

        List<Map<String, Object>> dataList = sysUserService.listSysUser(param);
        if (CollUtil.isEmpty(dataList)) {
            return fail(HttpMsg.NO_DATA);
        }

        Long count = sysUserService.countSysUser(param);

        return CommonUtil.putPageExtraFalse(dataList, count, param);
    }

    @ResponseBody
    @GetMapping(value = "/info/{userId}/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "系统用户信息", notes = "系统用户信息")
    @RequiresPermissions("sys:user:info")
    public Response info(@PathVariable("userId") String userId) {
        QueryWrapper<SysUser> sysUserQw = new QueryWrapper<>();
        sysUserQw.select("user_id, user_name, avatar, mobile, salt, identity, state");
        sysUserQw.eq(Const.DB_USER_ID, userId);

        SysUser sysUser = sysUserService.getOne(sysUserQw);
        if (ObjectUtil.isNotNull(sysUser)) {
            sysUser.setSalt(null);
            sysUser.setPassword(null);
        }

        // 查询用户对应的角色
        QueryWrapper<SysUserRole> sysUserRoleQw = new QueryWrapper<>();
        sysUserRoleQw.select("role_id");
        sysUserRoleQw.eq("user_id", userId);
        List<SysUserRole> sysUserRoles = sysUserRoleService.list(sysUserRoleQw);
        List<String> roleList = Lists.newArrayList();
        for (SysUserRole sysUserRole : sysUserRoles) {
            roleList.add(Convert.toStr(sysUserRole.getRoleId()));
        }

        sysUser.setRoleList(roleList);

        return objectReturn(sysUser);
    }
}
