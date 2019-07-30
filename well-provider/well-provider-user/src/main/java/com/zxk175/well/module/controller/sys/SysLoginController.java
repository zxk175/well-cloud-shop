package com.zxk175.well.module.controller.sys;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.dto.sys.SysUserLoginDTO;
import com.zxk175.well.common.model.param.sys.user.SysUserLoginParam;
import com.zxk175.well.common.model.param.sys.user.SysUserPermsParam;
import com.zxk175.well.common.util.jwt.JwTokenUtil;
import com.zxk175.well.common.util.jwt.bean.SysSubjectDTO;
import com.zxk175.well.common.util.jwt.bean.TokenDTO;
import com.zxk175.well.module.controller.BaseController;
import com.zxk175.well.module.entity.sys.SysUser;
import com.zxk175.well.module.service.sys.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author zxk175
 * @since 2019/04/01 15:49
 */
@Controller
@RequestMapping(Const.BASE_URL + "/sys")
@Api(tags = "SysLogin", description = "系统用户V1")
public class SysLoginController extends BaseController {

    @Autowired
    private SysUserService sysUserService;


    @ResponseBody
    @PostMapping(value = "/login/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "系统用户登录", notes = "系统用户登录")
    public Response login(@Validated @RequestBody SysUserLoginParam param) {
        // 用户信息
        QueryWrapper<SysUser> userQW = new QueryWrapper<>();
        userQW.select("user_id AS userId, user_name AS userName, avatar, mobile, salt, password, identity, state");
        userQW.eq("mobile", param.getMobile());
        SysUser sysUserDB = sysUserService.getOne(userQW);

        // 账号不存在
        if (ObjectUtil.isNull(sysUserDB)) {
            return fail("账号不存在");
        }

        // 账号锁定
        if (Const.ZERO == sysUserDB.getState()) {
            return fail("账号已被锁定,请联系管理员");
        }

        // 账号存在
        Sha256Hash sha256Hash = new Sha256Hash(param.getPassword(), sysUserDB.getSalt());
        if (ObjectUtil.isNotNull(sysUserDB) && sysUserDB.getPassword().equals(sha256Hash.toHex())) {
            SysSubjectDTO sysSubjectDTO = new SysSubjectDTO(sysUserDB.getUserId(), sysUserDB.getIdentity());
            TokenDTO tokenDTO = JwTokenUtil.buildToken(sysSubjectDTO);

            SysUserLoginDTO userDTO = new SysUserLoginDTO();
            BeanUtil.copyProperties(sysUserDB, userDTO);

            Map<Object, Object> data = Maps.newHashMap();
            data.put("user", userDTO);
            data.put("token", tokenDTO);

            SysUserPermsParam permsParam = new SysUserPermsParam(userDTO.getUserId());
            permsParam.setHasSupper(sysSubjectDTO.hasSupper());
            data.put("perms", sysUserService.setUserPerms(permsParam));

            return ok(data);
        }

        return fail("账号或密码不正确");
    }
}
