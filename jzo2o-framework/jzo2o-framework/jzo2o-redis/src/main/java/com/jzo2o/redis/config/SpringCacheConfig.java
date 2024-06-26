package com.jzo2o.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jzo2o.common.utils.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * SpringCache配置
 *
 * @author itcast
 * @create 2023/8/15 10:04
 **/
@Configuration
public class SpringCacheConfig {

    /**
     * 缓存时间30分钟
     *
     * @param connectionFactory redis连接工厂
     * @return redis缓存管理器
     */
    @Bean
    public RedisCacheManager cacheManager30Minutes(RedisConnectionFactory connectionFactory) {
        int randomNum = new Random().nextInt(100);
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30 * 60L + randomNum))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(JACKSON_SERIALIZER));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

    /**
     * 缓存时间1天
     *
     * @param connectionFactory redis连接工厂
     * @return redis缓存管理器
     */
    @Bean
    public RedisCacheManager cacheManagerOneDay(RedisConnectionFactory connectionFactory) {
        //生成随机数
        int randomNum = new Random().nextInt(6000);
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                //过期时间为基础时间加随机数
                .entryTtl(Duration.ofSeconds(24 * 60 * 60L + randomNum))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(JACKSON_SERIALIZER));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

    /**
     * 永久缓存
     *
     * @param connectionFactory redis连接工厂
     * @return redis缓存管理器
     */
    @Bean
    @Primary
    public RedisCacheManager cacheManagerForever(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(JACKSON_SERIALIZER));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

    private static final Jackson2JsonRedisSerializer<Object> JACKSON_SERIALIZER;

    static {
        //定义Jackson类型序列化对象
        JACKSON_SERIALIZER = new Jackson2JsonRedisSerializer<>(Object.class);
        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();

        // SimpleModule对象，添加各种序列化器和反序列化器。解决LocalDateTime、Long序列化异常
        SimpleModule simpleModule = new SimpleModule()
                // 添加反序列化器
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)))
                // 添加序列化器
                .addSerializer(BigInteger.class, ToStringSerializer.instance)
                .addSerializer(Long.class, ToStringSerializer.instance)   // 实现 Long --> String 的序列化器
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)));
        om.registerModule(simpleModule);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL
                , JsonTypeInfo.As.WRAPPER_ARRAY);
        JACKSON_SERIALIZER.setObjectMapper(om);
    }
}


