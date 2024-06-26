package com.jzo2o.orders.base.properties;

import com.jzo2o.common.utils.ObjectUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "executor")
@Data
public class ExecutorProperties {

    /**
     * 线程池配置
     */
    @NestedConfigurationProperty
    private Map<String, ThreadPool> pools = new HashMap<>();

    /**
     * 线程池配置
     */
    @Data
    public static class ThreadPool {
        /**
         * 核心线程数 默认15
         */
        private Integer corePoolSize = 15;

        /**
         * 最大线程数 默认30
         */
        private Integer maxPoolSize = 30;

        /**
         * 队列大小默认 5000
         */
        private Integer queueCapacity = 5000;

        /**
         * 线程名称，默认orders-
         */
        private String threadNamePrefix = "orders-";
    }

    /**
     * 线程池名称
     *
     * @param name
     * @return
     */
    public final ThreadPool get(String name) {
        ThreadPool threadPool = pools.get(name);
        return ObjectUtils.isNull(threadPool) ? new ThreadPool() : threadPool;
    }


}
