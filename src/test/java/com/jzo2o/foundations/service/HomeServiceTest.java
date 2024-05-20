package com.jzo2o.foundations.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class HomeServiceTest {
//    @Resource
//    private HomeService homeService;

//    @Test
//    void queryServeIconCategoryByCityCodeCache() {
//        List<ServeCategoryResDTO> list = homeService.queryServeIconCategoryByCityCodeCache("010");
//        System.out.println(list);
//    }
//
//    @Test
//    void findHotServeListByCityCodeCache() {
//        List<ServeAggregationSimpleResDTO> list = homeService.findHotServeListByCityCodeCache("023");
//        System.out.println(list);
//    }
//
//    @Test
//    void serveTypeList() {
//        List<ServeAggregationTypeSimpleResDTO> list = homeService.queryServeTypeListByCityCodeCache("010");
//        System.out.println(list);
//    }
//
//    @Test
//    public void test_queryServeIconCategoryByRegionIdCache(){
//
//        List<ServeCategoryResDTO> serveCategoryResDTOS = homeService.queryServeIconCategoryByRegionIdCache(1686303222843662337L);
//        System.out.println(serveCategoryResDTOS);
//
//    }
//
//    @Test
//    void test() throws JsonProcessingException {
//        List<ServeCategoryResDTO> list = new ArrayList<>();
//
//        ServeCategoryResDTO serveCategoryResDTO1 = new ServeCategoryResDTO();
//        serveCategoryResDTO1.setServeTypeId(123L);
//
//        ServeCategoryResDTO serveCategoryResDTO2 = new ServeCategoryResDTO();
//        serveCategoryResDTO2.setServeTypeId(456L);
//        list.add(serveCategoryResDTO1);
//        list.add(serveCategoryResDTO2);
//        list = new ArrayList<>(list.subList(0, 1));
//
//        ObjectMapper om = new ObjectMapper();
//
//        // SimpleModule对象，添加各种序列化器和反序列化器。解决LocalDateTime、Long序列化异常
//        SimpleModule simpleModule = new SimpleModule()
//                // 添加反序列化器
//                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)))
//                // 添加序列化器
//                .addSerializer(BigInteger.class, ToStringSerializer.instance)
//                .addSerializer(Long.class, ToStringSerializer.instance)   // 实现 Long --> String 的序列化器
//                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)));
//        om.registerModule(simpleModule);
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL
//                , JsonTypeInfo.As.WRAPPER_ARRAY);
//
//        String s = om.writeValueAsString(list);
//        System.out.println(s);
//    }
//
//
//    private static final Jackson2JsonRedisSerializer<Object> JACKSON_SERIALIZER;
//
//    static {
//        //定义Jackson类型序列化对象
//        JACKSON_SERIALIZER = new Jackson2JsonRedisSerializer<>(Object.class);
//        //解决查询缓存转换异常的问题
//        ObjectMapper om = new ObjectMapper();
//
//        // SimpleModule对象，添加各种序列化器和反序列化器。解决LocalDateTime、Long序列化异常
//        SimpleModule simpleModule = new SimpleModule()
//                // 添加反序列化器
//                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)))
//                // 添加序列化器
//                .addSerializer(BigInteger.class, ToStringSerializer.instance)
//                .addSerializer(Long.class, ToStringSerializer.instance)   // 实现 Long --> String 的序列化器
//                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)));
//
//        om.registerModule(simpleModule);
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL
//                , JsonTypeInfo.As.WRAPPER_ARRAY);
//        JACKSON_SERIALIZER.setObjectMapper(om);
//    }

}