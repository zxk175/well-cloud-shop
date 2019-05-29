package com.zxk175.well.module.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxk175.well.common.model.param.sys.role.SysRoleListParam;
import com.zxk175.well.module.entity.sys.SysRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
public interface SysRoleDao extends BaseMapper<SysRole> {

    List<Map<String, Object>> listSysRole(SysRoleListParam param);

    Long countSysRole(SysRoleListParam param);
}
