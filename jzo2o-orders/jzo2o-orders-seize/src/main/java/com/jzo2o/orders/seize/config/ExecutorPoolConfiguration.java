package com.jzo2o.orders.seize.config;

import com.jzo2o.orders.base.properties.ExecutorProperties;
import com.jzo2o.orders.seize.constants.ThreadPoolName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorPoolConfiguration {


    /**
     * 抢单同步线程池
     *
     * @param executorProperties
     * @return
     */
    @Bean("seizeSyncExecutor")
    public Executor dispatchSyncExecutor(ExecutorProperties executorProperties) {
        ExecutorProperties.ThreadPool dispatchSyncThreadPool = executorProperties.get(ThreadPoolName.SEIZE_SYNC);
        ThreadPoolTaskExecutor dispatchExecutor = new ThreadPoolTaskExecutor();
        dispatchExecutor.setCorePoolSize(dispatchSyncThreadPool.getCorePoolSize());
        dispatchExecutor.setMaxPoolSize(dispatchSyncThreadPool.getMaxPoolSize());
        dispatchExecutor.setQueueCapacity(dispatchSyncThreadPool.getQueueCapacity());
        dispatchExecutor.setThreadNamePrefix(dispatchSyncThreadPool.getThreadNamePrefix());
        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        dispatchExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        dispatchExecutor.initialize();
        return dispatchExecutor;
    }
}
