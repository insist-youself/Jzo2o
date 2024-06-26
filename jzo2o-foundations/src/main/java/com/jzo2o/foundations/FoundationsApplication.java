package com.jzo2o.foundations;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author itcast
 */
@Slf4j
@EnableCaching
@MapperScan("com.jzo2o.foundations.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy
public class FoundationsApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(FoundationsApplication.class)
                .build(args)
                .run(args);
        log.info("家政服务-运营基础服务启动");
    }
}
