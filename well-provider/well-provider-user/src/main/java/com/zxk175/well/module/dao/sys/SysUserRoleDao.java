package com.zxk175.well.module.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxk175.well.common.model.param.sys.role.SysRoleListParam;
import com.zxk175.well.module.entity.sys.SysUserRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户与角色关联表 Mapper 接口
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
public interface SysUserRoleDao extends BaseMapper<SysUserRole> {

    List<Map<String, Object>> listSysRole(SysRoleListParam param);

    Long countSysRole(SysRoleListParam param);
}
