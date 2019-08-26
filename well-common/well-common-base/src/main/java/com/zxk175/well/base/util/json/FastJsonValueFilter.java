package com.zxk175.well.base.util.json;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.zxk175.well.base.util.MyStrUtil;

/**
 * @author zxk175
 * @since 2019-08-11 01:07
 */
public class FastJsonValueFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        boolean flag = ObjectUtil.isNull(value) && ("data".equals(name) || "extra".equals(name));
        if (flag) {
            return new Object();
        }

        if (ObjectUtil.isNull(value)) {
            return MyStrUtil.EMPTY;
        }

        return value;
    }
}
