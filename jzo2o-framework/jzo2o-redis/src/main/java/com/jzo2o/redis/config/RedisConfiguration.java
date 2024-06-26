package com.jzo2o.redis.config;

import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.redis.aspect.HashCacheClearAspect;
import com.jzo2o.redis.helper.CacheHelper;
import com.jzo2o.redis.helper.LockHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author itcast
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(RedisProperties.class)
@Import({CacheHelper.class, LockHelper.class})
public class RedisConfiguration {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static{
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)));
        timeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)));
        timeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)));
        timeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)));
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.registerModule(timeModule);
    }


    @Bean("redisTemplate")
    @Primary
    public RedisTemplate<String, Object> restTemplate(RedisConnectionFactory redisConnnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        log.info("redisTemplate hashCode : {}", redisTemplate.hashCode());
        redisTemplate.setConnectionFactory(redisConnnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericToStringSerializer(String.class));
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(OBJECT_MAPPER));
        return redisTemplate;
    }

    @Bean
    public HashCacheClearAspect hashCacheClearAspect(CacheHelper cacheHelper) {
        return new HashCacheClearAspect(cacheHelper);
    }
}
