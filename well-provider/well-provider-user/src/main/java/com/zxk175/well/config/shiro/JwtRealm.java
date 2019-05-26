package com.zxk175.well.config.shiro;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.zxk175.well.common.model.param.sys.user.SysUserPermsParam;
import com.zxk175.well.common.util.jwt.JwtAuthUtil;
import com.zxk175.well.common.util.jwt.bean.SysSubjectDTO;
import com.zxk175.well.module.service.sys.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author zxk175
 * @since 2019/03/23 20:27
 */
@Component
public class JwtRealm extends AuthorizingRealm {

    private final SysUserService sysUserService;


    @Lazy
    public JwtRealm(SysUserService sysUserService) {
        // @Lazy用处
        // https://blog.csdn.net/finalcola/article/details/81197584
        this.sysUserService = sysUserService;
    }

    // 大坑！，必须重写此方法，不然shiro会报错
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    // 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = principals.toString();

        // 解密获得userId，用于和数据库进行对比
        SysSubjectDTO tokenSubject = parseToken(token);
        String userId = Convert.toStr(tokenSubject.getUserId());

        // 获取用户权限
        SysUserPermsParam permsParam = new SysUserPermsParam();
        permsParam.setUserId(userId);
        permsParam.setHasSupper(tokenSubject.hasSupper());
        Set<String> permissions = sysUserService.setUserPerms(permsParam);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    // 默认使用此方法进行用户名正确与否验证，错误抛出异常即可
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = auth.getPrincipal().toString();

        // 解密获得userId，用于和数据库进行对比
        parseToken(token);

        return new SimpleAuthenticationInfo(token, token, getName());
    }

    private SysSubjectDTO parseToken(String token) {
        // 解密获得userId，用于和数据库进行对比
        SysSubjectDTO tokenSubject = JwtAuthUtil.sysSubject(token);
        if (ObjectUtil.isNull(tokenSubject)) {
            throw new AuthenticationException("token不合法");
        }

        return tokenSubject;
    }
}
