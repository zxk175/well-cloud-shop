package com.zxk175.well.module.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxk175.well.common.model.param.sys.user.SysUserListParam;
import com.zxk175.well.common.model.param.sys.user.SysUserPermsParam;
import com.zxk175.well.module.entity.sys.SysUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
public interface SysUserDao extends BaseMapper<SysUser> {

    List<Map<String, Object>> listSysUser(SysUserListParam param);

    Long countSysUser(SysUserListParam param);

    List<String> listUserPerms(SysUserPermsParam param);
}
