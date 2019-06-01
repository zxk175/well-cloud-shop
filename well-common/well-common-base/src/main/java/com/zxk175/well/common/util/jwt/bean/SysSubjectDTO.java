package com.zxk175.well.common.util.jwt.bean;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.zxk175.well.common.consts.enums.IdentityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zxk175
 * @since 2019/04/13 16:18
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class SysSubjectDTO {

    private Long userId;

    private Integer identity;


    public String getUserIdStr() {
        return Convert.toStr(userId);
    }

    public Integer getIdentity() {
        return ObjectUtil.isNull(identity) ? IdentityType.ORDINARY.value() : identity;
    }

    public boolean hasSupper() {
        return IdentityType.SUPER.value().equals(getIdentity());
    }

    public boolean hasOrdinary() {
        return IdentityType.ORDINARY.value().equals(getIdentity());
    }
}
