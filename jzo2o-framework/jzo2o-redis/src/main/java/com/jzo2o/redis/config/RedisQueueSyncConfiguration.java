package com.jzo2o.redis.config;

import com.jzo2o.redis.properties.RedisSyncProperties;
import com.jzo2o.redis.sync.impl.SyncManagerImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(RedisSyncProperties.class)
@Import(SyncManagerImpl.class)
public class RedisQueueSyncConfiguration {
}
