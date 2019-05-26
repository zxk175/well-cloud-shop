package com.zxk175.well.common.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author zxk175
 * @since 2019/04/13 16:04
 */
public class ThreadUtil {

    /**
     * 新建一个线程池
     *
     * @param threadSize 同时执行的线程数大小
     * @param threadName 线程名字
     * @return ExecutorService
     */
    public static ExecutorService newExecutor(int threadSize, String threadName) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadName + "-pool-%d")
                .build();

        return new ThreadPoolExecutor(
                threadSize,
                threadSize,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }
}
