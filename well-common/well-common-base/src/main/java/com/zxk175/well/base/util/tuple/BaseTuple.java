package com.zxk175.well.base.util.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;

/**
 * @author zxk175
 * @since 2019-08-26 16:39
 */
public abstract class BaseTuple implements Iterable<Object>, Serializable {

    private final List<Object> valueList;

    BaseTuple(final Object... objects) {
        //其实就是简单的数组，只是包装成List，方便使用List的api进行元素操作
        this.valueList = Arrays.asList(objects);
    }

    @Override
    public final Iterator<Object> iterator() {
        return this.valueList.iterator();
    }

    @Override
    public final Spliterator<Object> spliterator() {
        return this.valueList.spliterator();
    }

    /**
     * 反转元组
     * 反转后元组大小不变，子类各自实现可以达到最好性能，也可以指定返回值类型，方便使用
     *
     * @return 反转后的元组
     */
    public abstract BaseTuple swap();
}
