package com.zxk175.well.module.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxk175.well.common.model.param.sys.user.SysUserPermsParam;
import com.zxk175.well.module.entity.sys.SysUser;

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

    Set<String> setUserPerms(SysUserPermsParam param);
}
