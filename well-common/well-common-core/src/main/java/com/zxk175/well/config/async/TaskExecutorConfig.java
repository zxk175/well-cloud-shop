package com.zxk175.well.config.async;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zxk175
 * @since 2019/04/13 17:50
 */
@Configuration
public class TaskExecutorConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
        executor.setThreadNamePrefix("Annotation-Exec");
        // 连接池中保留的最大连接数。Default: 15 maxPoolSize
        executor.setMaxPoolSize(200);
        // 如果池中的实际线程数小于corePoolSize,无论是否其中有空闲的线程,都会给新的任务产生新的线程
        executor.setCorePoolSize(10);
        // 线程池所使用的缓冲队列
        executor.setQueueCapacity(10);

        // 使用预定义的异常处理类
        // rejectionPolicy：当pool已经达到max size的时候，如何处理新任务
        // callerRunsPolicy：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();

        return executor;
    }
}
