package com.jzo2o.orders.seize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author itcast
 */
@Configuration
public class RedisLuaConfiguration {

    @Bean("seizeOrdersScript")
    public DefaultRedisScript<Integer> seizeOrders() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        //resource目录下的scripts文件下的seizeOrders.lua文件
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/seizeOrdersScript.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }
}
