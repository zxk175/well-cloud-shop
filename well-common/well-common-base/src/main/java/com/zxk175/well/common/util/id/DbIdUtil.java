package com.zxk175.well.common.util.id;

/**
 * Id生成器
 *
 * @author zxk175
 */
public class DbIdUtil {

    /**
     * 主机和进程的机器码
     */
    private static final Sequence WORKER = new Sequence(0, 0);

    /**
     * 获取数据库Id
     *
     * @return ignore
     */
    public static Long id() {
        return WORKER.nextId();
    }
}