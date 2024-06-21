package com.jzo2o.orders.dispatch.config;

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

    @Bean("dispatchOrdersScript")
    public DefaultRedisScript<String> dispatchOrdersScript() {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        //resource目录下的scripts文件下的dispatchOrdersScript.lua文件
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/dispatchOrdersScript.lua")));
        redisScript.setResultType(String.class);
        return redisScript;
    }
}
