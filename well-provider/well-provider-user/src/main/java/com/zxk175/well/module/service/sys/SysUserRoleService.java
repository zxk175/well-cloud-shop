package com.zxk175.well.module.service.sys;

import com.zxk175.well.module.entity.sys.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户与角色关联表 服务类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    void removeBatch(List<String> param, String by);

    void saveOrModify(Long userId, List<String> roleList);
}
