//package com.jzo2o.market.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author Mr.M
// * @version 1.0
// * @description TODO
// * @date 2023/10/13 16:28
// */
//@SpringBootTest
//@Slf4j
//public class RedisLuaTest {
//
//
//    @Resource(name = "redisTemplate")
//    RedisTemplate redisTemplate;
//
//    @Resource(name = "lua_test01")
//    DefaultRedisScript script;
//
//    //测试lua
//    @Test
//    public void test_luafirst() {
//        //参数1：key ,key1:test_key01  key2:test_key02
//        List<String> keys = Arrays.asList("test_key01","test_key02");
//        //参数2：传入lua脚本的参数,"field1","aa","field2", "bb"
//        Object result = redisTemplate.execute(script, keys, "field1","aa","field2", "bb");
//        log.info("执行结果:{}",result);
//    }
//
//    @Test
//    public void test_luafirst2() {
//        //参数1：key ,key1:test_key01
//        List<String> keys = Arrays.asList("test_key01{1}","test_key02{1}");
//        //参数2：传入lua脚本的参数,"field1","aa","field2", "bb"
//        Object result = redisTemplate.execute(script, keys, "field1","aa","field2", "bb");
//        log.info("执行结果:{}",result);
//    }
//
//}
//
