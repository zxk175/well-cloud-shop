package com.zxk175.well.base.util.id;

/**
 * @author zxk175
 * @since 2019-08-11 01:07
 */
public class DbIdUtil {

    /**
     * 主机和进程的机器码
     */
    private static final Sequence WORKER = new Sequence(0, 0);

    public static Long id() {
        return WORKER.nextId();
    }
}