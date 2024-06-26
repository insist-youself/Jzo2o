package com.jzo2o.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis.sync")
@Data
public class RedisSyncProperties {
    /**
     * 每批处理数量
     */
    private int perCount = 100;

    /**
     * 队列数
     */
    private int queueNum = 10;
}
