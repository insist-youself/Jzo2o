package com.jzo2o.redis.config;

import com.jzo2o.redis.aspect.LockAspect;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author itcast
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
@Slf4j
@EnableAspectJAutoProxy
public class LockConfiguration {

    /**
     * 实例化分布式锁切面实例
     *
     * @param redissonClient
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public LockAspect lockAspect(RedissonClient redissonClient) {
        log.debug("分布式锁切面实例化...");
        return new LockAspect(redissonClient);
    }

}
