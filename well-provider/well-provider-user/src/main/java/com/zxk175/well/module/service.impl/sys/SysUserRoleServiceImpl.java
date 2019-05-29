package com.zxk175.well.module.service.impl.sys;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.module.dao.sys.SysUserRoleDao;
import com.zxk175.well.module.entity.sys.SysUserRole;
import com.zxk175.well.module.service.sys.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户与角色关联表 服务实现类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void removeBatch(List<String> param, String by) {
        QueryWrapper<SysUserRole> sysUserRoleQW = new QueryWrapper<>();
        sysUserRoleQW.in(by, param);

        this.remove(sysUserRoleQW);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveOrModify(Long userId, List<String> roleList) {
        // 先删除用户与角色关联
        QueryWrapper<SysUserRole> sysUserRoleQW = new QueryWrapper<>();
        sysUserRoleQW.eq(Const.DB_USER_ID, userId);
        this.remove(sysUserRoleQW);

        List<SysUserRole> dataList = Lists.newArrayList();
        for (String roleId : roleList) {
            // 保存用户与角色关联
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(Convert.toLong(roleId));

            dataList.add(sysUserRole);
        }

        this.saveBatch(dataList, Const.DB_BATCH_SIZE);
    }
}
