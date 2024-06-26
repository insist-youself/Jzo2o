package com.jzo2o.orders.history;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@Slf4j
@EnableCaching
@MapperScan("com.jzo2o.orders.history.mapper")
public class OrdersHistoryApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(OrdersHistoryApplication.class)
                .build(args)
                .run(args);
        log.info("家政服务-历史订单微服务启动");
    }
}