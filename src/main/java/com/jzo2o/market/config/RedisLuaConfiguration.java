package com.jzo2o.market.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Objects;

@Configuration
public class RedisLuaConfiguration {

    @Bean("seizeCouponScript")
    public DefaultRedisScript<Integer> seizeCouponScript() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        //resource目录下的scripts文件下的seizeCouponScript.lua文件
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/seizeCouponScript.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }

    @Bean("lua_test01")
    public DefaultRedisScript<Integer> getLuaTest01() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        //resource目录下的scripts文件下的lua_test01.lua文件
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/lua_test01.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }

}
