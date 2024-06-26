package com.jzo2o.market.config;

import com.jzo2o.redis.properties.RedisSyncProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 线程池配置类
 *
 * @author linger
 * @date 2024/6/17 20:10
 */

@Configuration
public class ThreadPoolConfiguration {

    @Bean("syncThreadPool")
    public ThreadPoolExecutor synchronizeThreadPool(RedisSyncProperties redisSyncProperties) {
        // 定义线程池参数
        int corePoolSize = 1; //核心线程数
        int maximumPoolSize = redisSyncProperties.getQueueNum(); // 最大线程数
        long keepAliveTime = 120; // 线程空闲时间
        TimeUnit unit = TimeUnit.SECONDS; // 时间单位
        BlockingQueue<Runnable> workQueue = new SynchronousQueue<>(); // 阻塞队列
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy(); //默认拒绝策略, 指定拒绝策略为 DiscardPolicy

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue, handler);

        return threadPoolExecutor;
    }
}