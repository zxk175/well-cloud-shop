package com.zxk175.well.module.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.role.SysRoleListParam;
import com.zxk175.well.module.entity.sys.SysRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
public interface SysRoleService extends IService<SysRole> {

    Response saveRole(SysRole sysRole);

    Response removeRoles(List<String> param);

    Response modifyRole(SysRole sysRole);

    List<Map<String, Object>> listSysRole(SysRoleListParam param);

    Long countSysRole(SysRoleListParam param);
}
