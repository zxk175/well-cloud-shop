package com.zxk175.well.module.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.user.SysUserListParam;
import com.zxk175.well.common.model.param.sys.user.SysUserPermsParam;
import com.zxk175.well.module.entity.sys.SysUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
public interface SysUserService extends IService<SysUser> {

    Response saveUser(SysUser sysUser);

    Response removeUsers(List<String> param);

    Response modifyUser(SysUser sysUser);

    List<Map<String, Object>> listSysUser(SysUserListParam param);

    Long countSysUser(SysUserListParam param);

    Set<String> setUserPerms(SysUserPermsParam param);
}
