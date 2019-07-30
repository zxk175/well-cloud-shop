package com.zxk175.well.module.service.impl.sys;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.consts.enums.MenuType;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.model.param.sys.user.SysUserListParam;
import com.zxk175.well.common.model.param.sys.user.SysUserPermsParam;
import com.zxk175.well.common.util.MyStrUtil;
import com.zxk175.well.common.util.common.CommonUtil;
import com.zxk175.well.module.dao.sys.SysUserDao;
import com.zxk175.well.module.entity.sys.SysMenu;
import com.zxk175.well.module.entity.sys.SysUser;
import com.zxk175.well.module.service.sys.SysMenuService;
import com.zxk175.well.module.service.sys.SysUserRoleService;
import com.zxk175.well.module.service.sys.SysUserService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author zxk175
 * @since 2019-05-26 22:49:45
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserRoleService sysUserRoleService;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Response saveUser(SysUser sysUser) {
        String salt = CommonUtil.getRandom(false, 12);
        sysUser.setPassword(new Sha256Hash(sysUser.getPassword(), salt).toHex());
        sysUser.setSalt(salt);

        this.save(sysUser);

        boolean save = saveOrModify(sysUser);
        return Response.saveReturn(save);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Response removeUsers(List<String> param) {
        this.removeByIds(param);

        sysUserRoleService.removeBatch(param, Const.DB_USER_ID);

        return Response.removeReturn(true);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Response modifyUser(SysUser sysUser) {
        String password = sysUser.getPassword();
        if (StrUtil.isNotBlank(password)) {
            String salt = CommonUtil.getRandom(false, 12);
            sysUser.setPassword(new Sha256Hash(password, salt).toHex());
            sysUser.setSalt(salt);
        }

        this.updateById(sysUser);

        boolean modify = saveOrModify(sysUser);
        return Response.modifyReturn(modify);
    }

    @Override
    public List<Map<String, Object>> listSysUser(SysUserListParam param) {
        return baseMapper.listSysUser(param);
    }

    @Override
    public Long countSysUser(SysUserListParam param) {
        return baseMapper.countSysUser(param);
    }

    @Override
    public Set<String> setUserPerms(SysUserPermsParam param) {
        List<String> permsList;
        if (param.hasSupper()) {
            QueryWrapper<SysMenu> sysMenuQW = new QueryWrapper<>();
            sysMenuQW.select("menu_id, perms");
            sysMenuQW.eq("type", MenuType.BUTTON.value());
            sysMenuQW.eq(Const.DB_STATE, Const.ONE);

            List<SysMenu> menuList = sysMenuService.list(sysMenuQW);
            permsList = Lists.newArrayListWithCapacity(menuList.size());
            for (SysMenu sysMenu : menuList) {
                permsList.add(sysMenu.getPerms());
            }
        } else {
            permsList = baseMapper.listUserPerms(param);
        }

        // 用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (MyStrUtil.isBlank(perms)) {
                continue;
            }

            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        return permsSet;
    }

    private boolean saveOrModify(SysUser sysUser) {
        Long userId = sysUser.getUserId();

        // 保存角色与菜单关系
        sysUserRoleService.saveOrModify(userId, sysUser.getRoleList());

        return true;
    }
}
