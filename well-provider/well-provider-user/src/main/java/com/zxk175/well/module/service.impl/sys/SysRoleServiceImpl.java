package com.zxk175.well.module.service.impl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.role.SysRoleListParam;
import com.zxk175.well.module.dao.sys.SysRoleDao;
import com.zxk175.well.module.entity.sys.SysRole;
import com.zxk175.well.module.service.sys.SysRoleMenuService;
import com.zxk175.well.module.service.sys.SysRoleService;
import com.zxk175.well.module.service.sys.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysUserRoleService sysUserRoleService;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Response saveRole(SysRole sysRole) {
        this.save(sysRole);

        boolean save = saveOrModify(sysRole);
        return Response.saveReturn(save);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Response removeRoles(List<String> param) {
        // 删除角色
        this.removeByIds(param);

        // 删除角色与菜单关联
        sysRoleMenuService.removeBatch(param);

        // 删除角色与用户关联
        sysUserRoleService.removeBatch(param, "role_id");

        return Response.removeReturn(true);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Response modifyRole(SysRole sysRole) {
        this.updateById(sysRole);

        boolean modify = saveOrModify(sysRole);
        return Response.modifyReturn(modify);
    }

    @Override
    public List<Map<String, Object>> listSysRole(SysRoleListParam param) {
        return baseMapper.listSysRole(param);
    }

    @Override
    public Long countSysRole(SysRoleListParam param) {
        return baseMapper.countSysRole(param);
    }

    private boolean saveOrModify(SysRole sysRole) {
        Long roleId = sysRole.getRoleId();

        // 保存角色与菜单关系
        sysRoleMenuService.saveOrModify(roleId, sysRole.getMenuList());

        return true;
    }
}
