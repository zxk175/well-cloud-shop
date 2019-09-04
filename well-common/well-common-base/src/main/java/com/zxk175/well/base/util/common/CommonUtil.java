package com.zxk175.well.base.util.common;

import cn.hutool.core.collection.CollUtil;
import com.zxk175.well.base.bean.dto.PageBeanDto;
import com.zxk175.well.base.bean.param.BasePageParam;
import com.zxk175.well.base.http.HttpMsg;
import com.zxk175.well.base.http.Response;

import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.Collection;

/**
 * @author zxk175
 * @since 2019/04/01 16:28
 */
public class CommonUtil {

    public static void buildPageParam(BasePageParam param) {
        Long[] longs = transToStartEnd(param.getPage(), param.getSize());

        param.setPage(longs[0]);
        param.setSize(longs[1]);
    }

    private static Long[] transToStartEnd(Long page, Long size) {
        // 最多一次100条数据
        long tmp = 100;
        if (size > tmp) {
            size = tmp;
        }

        if (page < 1) {
            page = 1L;
        }

        if (size <= -1) {
            size = 1000L;
        }

        long start = (page - 1) * size;
        long end = size;

        return new Long[]{start, end};
    }

    public static <T> Response<PageBeanDto<T>> putPageTrue(T data, Long total, BasePageParam param) {
        return putPageExtra(true, data, total, param);
    }

    public static <T> Response<PageBeanDto<T>> putPageFalse(T data, Long total, BasePageParam param) {
        return putPageExtra(false, data, total, param);
    }

    private static <T> Response<PageBeanDto<T>> putPageExtra(boolean putTotal, T data, Long total, BasePageParam param) {
        PageBeanDto<T> pageBeanDto = new PageBeanDto<>();
        pageBeanDto.setPage(param.getPage() + 1);
        pageBeanDto.setSize(param.getSize());
        pageBeanDto.setRecords(data);

        if (putTotal) {
            pageBeanDto.setTotal(0L);
            pageBeanDto.setPages(0L);
        } else {
            pageBeanDto.setTotal(total);
        }

        // 得到泛型T--父类型
        Type genericSuperclass = data.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof AbstractList) {
            if (CollUtil.isEmpty((Collection<?>) data)) {
                return Response.fail(HttpMsg.NO_DATA);
            }
        }

        return Response.ok(pageBeanDto);
    }
}
