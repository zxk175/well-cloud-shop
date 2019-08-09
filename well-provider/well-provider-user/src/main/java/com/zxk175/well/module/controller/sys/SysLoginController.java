package com.zxk175.well.module.controller.sys;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.sd4324530.jtuple.Tuple2;
import com.google.common.collect.Maps;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.dto.sys.SysUserLoginDTO;
import com.zxk175.well.common.model.param.sys.user.SysUserLoginParam;
import com.zxk175.well.common.model.param.sys.user.SysUserPermsParam;
import com.zxk175.well.common.util.ShaUtils;
import com.zxk175.well.common.util.jwt.JwTokenUtil;
import com.zxk175.well.common.util.jwt.bean.SysSubjectDTO;
import com.zxk175.well.common.util.jwt.bean.TokenDTO;
import com.zxk175.well.module.controller.BaseController;
import com.zxk175.well.module.entity.sys.SysUser;
import com.zxk175.well.module.service.sys.SysUserService;
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

import java.util.Map;

/**
 * @author zxk175
 * @since 2019/04/01 15:49
 */
@Controller
@AllArgsConstructor
@RequestMapping(Const.BASE_URL + "/sys")
@Api(tags = "SysLogin", description = "系统用户V1")
public class SysLoginController extends BaseController {

    private SysUserService sysUserService;


    @ResponseBody
    @PostMapping(value = "/login/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "系统用户登录", notes = "系统用户登录")
    public Response login(@Validated @RequestBody SysUserLoginParam param) {
        // 用户信息
        QueryWrapper<SysUser> userQw = new QueryWrapper<>();
        userQw.select("user_id AS userId, user_name AS userName, avatar, mobile, salt, password, identity, state");
        userQw.eq("mobile", param.getMobile());
        SysUser sysUserDb = sysUserService.getOne(userQw);

        // 账号不存在
        if (ObjectUtil.isNull(sysUserDb)) {
            return fail("账号不存在");
        }

        // 账号锁定
        if (Const.ZERO == sysUserDb.getState()) {
            return fail("账号已被锁定,请联系管理员");
        }

        // 账号存在
        Tuple2<String, String> tuple = ShaUtils.enc(param.getPassword(), sysUserDb.getSalt());
        if (ObjectUtil.isNotNull(sysUserDb) && sysUserDb.getPassword().equals(tuple.first)) {
            SysSubjectDTO sysSubjectDTO = new SysSubjectDTO(sysUserDb.getUserId(), sysUserDb.getIdentity());
            TokenDTO tokenDTO = JwTokenUtil.buildToken(sysSubjectDTO);

            SysUserLoginDTO userDTO = new SysUserLoginDTO();
            BeanUtil.copyProperties(sysUserDb, userDTO);

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
