package com.zxk175.well.config.async;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * async线程中访问RequestContextHolder
 *
 * @author zxk175
 * https://segmentfault.com/a/1190000009625348
 */
public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Callable<T> callable) {
        return super.submit(new ContextAwareCallable<>(callable, RequestContextHolder.currentRequestAttributes()));
    }

    @NonNull
    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> callable) {
        return super.submitListenable(new ContextAwareCallable<>(callable, RequestContextHolder.currentRequestAttributes()));
    }
}