package com.jzo2o.publics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 通用服务启动类
 *
 * @author itcast
 * @create 2023/8/23 17:14
 **/
@Slf4j
@SpringBootApplication
public class PublicsApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(PublicsApplication.class)
                .build(args)
                .run(args);
        log.info("家政服务-通用服务启动");
    }
}
