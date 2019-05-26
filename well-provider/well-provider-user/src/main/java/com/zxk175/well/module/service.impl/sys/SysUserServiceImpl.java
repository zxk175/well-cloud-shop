package com.zxk175.well.module.service.impl.sys;

import com.zxk175.well.common.model.param.sys.user.SysUserPermsParam;
import com.zxk175.well.module.entity.sys.SysUser;
import com.zxk175.well.module.dao.sys.SysUserDao;
import com.zxk175.well.module.service.sys.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public Set<String> setUserPerms(SysUserPermsParam param) {
        return null;
    }
}
