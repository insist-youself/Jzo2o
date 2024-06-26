package com.jzo2o.mvc.config;

import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.mvc.serialize.BigDecimalSerializer;
import com.jzo2o.mvc.serialize.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.jzo2o.common.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

/**
 * @author itcast
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class JsonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
            jacksonObjectMapperBuilder.timeZone(DateUtils.TIME_ZONE_8);
            jacksonObjectMapperBuilder.serializers(new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer(dateTimeFormatter));
            jacksonObjectMapperBuilder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
            // long -> string
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            // bigDecimal保留两位小数
            jacksonObjectMapperBuilder.serializerByType(BigDecimal.class, BigDecimalSerializer.instance);
            // LocalDateTime 格式yyyy-MM-dd HH:mm:ss
            jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, LocalDateTimeSerializer.instance);
        };
    }
}
