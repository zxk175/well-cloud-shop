package com.zxk175.well.core.util.tuple;

/**
 * @author zxk175
 * @since 2019-08-26 16:34
 */
public final class Tuples {

    private Tuples() {
    }

    /**
     * 创建Tuple2
     *
     * @param first  元素
     * @param second 元素
     * @param <A>    元素泛型
     * @param <B>    元素泛型
     * @return Tuple2
     */
    public static <A, B> Tuple2<A, B> tuple(final A first, final B second) {
        return Tuple2.with(first, second);
    }

    /**
     * 创建Tuple3
     *
     * @param first  元素
     * @param second 元素
     * @param third  元素
     * @param <A>    元素泛型
     * @param <B>    元素泛型
     * @param <C>    元素泛型
     * @return Tuple3
     */
    public static <A, B, C> Tuple3<A, B, C> tuple(final A first, final B second, final C third) {
        return Tuple3.with(first, second, third);
    }

    /**
     * 创建Tuple4
     *
     * @param first  元素
     * @param second 元素
     * @param third  元素
     * @param fourth 元素
     * @param <A>    元素泛型
     * @param <B>    元素泛型
     * @param <C>    元素泛型
     * @param <D>    元素泛型
     * @return Tuple4
     */
    public static <A, B, C, D> Tuple4<A, B, C, D> tuple(final A first, final B second, final C third, final D fourth) {
        return Tuple4.with(first, second, third, fourth);
    }
}
