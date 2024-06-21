package com.jzo2o.orders.dispatch.config;

import com.jzo2o.orders.base.properties.ExecutorProperties;
import com.jzo2o.orders.dispatch.constants.ThreadPoolName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorPoolConfiguration {

    /**
     * 派单线程池
     * @param executorProperties
     * @return
     */
    @Bean("dispatchExecutor")
    public Executor dispatchExecutor(ExecutorProperties executorProperties) {
        ExecutorProperties.ThreadPool dispatchThreadPool = executorProperties.get(ThreadPoolName.DISPATCH);
        ThreadPoolTaskExecutor dispatchExecutor = new ThreadPoolTaskExecutor();
        dispatchExecutor.setCorePoolSize(dispatchThreadPool.getCorePoolSize());
        dispatchExecutor.setMaxPoolSize(dispatchThreadPool.getMaxPoolSize());
        dispatchExecutor.setQueueCapacity(dispatchThreadPool.getQueueCapacity());
        dispatchExecutor.setThreadNamePrefix(dispatchThreadPool.getThreadNamePrefix());
        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        dispatchExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        dispatchExecutor.initialize();
        return dispatchExecutor;
    }

    /**
     * 派单同步线程池
     *
     * @param executorProperties
     * @return
     */
    @Bean("dispatchSyncExecutor")
    public Executor dispatchSyncExecutor(ExecutorProperties executorProperties) {
        ExecutorProperties.ThreadPool dispatchSyncThreadPool = executorProperties.get(ThreadPoolName.DISPATCH_SYNC);
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

    @Bean("receiveTimeoutExecutor")
    public Executor receiveTimeoutExecutor(ExecutorProperties executorProperties) {
        ExecutorProperties.ThreadPool dispatchSyncThreadPool = executorProperties.get(ThreadPoolName.RECEIVE_TIMEOUT);
        ThreadPoolTaskExecutor receiveTimeoutExecutor = new ThreadPoolTaskExecutor();
        receiveTimeoutExecutor.setCorePoolSize(dispatchSyncThreadPool.getCorePoolSize());
        receiveTimeoutExecutor.setMaxPoolSize(dispatchSyncThreadPool.getMaxPoolSize());
        receiveTimeoutExecutor.setQueueCapacity(dispatchSyncThreadPool.getQueueCapacity());
        receiveTimeoutExecutor.setThreadNamePrefix(dispatchSyncThreadPool.getThreadNamePrefix());
        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        receiveTimeoutExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        receiveTimeoutExecutor.initialize();
        return receiveTimeoutExecutor;
    }
}
