package com.zxk175.well.common.util.id;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

/**
 * 高并发场景下System.currentTimeMillis()的性能问题的优化
 * System.currentTimeMillis()的调用比new一个普通对象要耗时的多（具体耗时高出多少我还没测试过，有人说是100倍左右）
 * System.currentTimeMillis()之所以慢是因为去跟系统打了一次交道
 * 后台定时更新时钟，JVM退出时，线程自动回收
 * 10亿：43410,206,210.72815533980582%
 * 1亿：4699,29,162.0344827586207%
 * 1000万：480,12,40.0%
 * 100万：50,10,5.0%
 *
 * @author lry
 * @since 2019/03/23 15:43
 */
public class ClockUtil {

    private final long period;
    private final AtomicLong now;

    private ClockUtil(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    private static ClockUtil instance() {
        return InstanceHolder.INSTANCE;
    }

    public static long now() {
        return instance().currentTimeMillis();
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, getClass().getName());
            thread.setDaemon(true);
            return thread;
        });

        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    private long currentTimeMillis() {
        return now.get();
    }

    private static class InstanceHolder {
        static final ClockUtil INSTANCE = new ClockUtil(1);
    }
}
