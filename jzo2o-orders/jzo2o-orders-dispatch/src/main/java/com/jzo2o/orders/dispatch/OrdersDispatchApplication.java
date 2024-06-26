package com.jzo2o.orders.dispatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author itcast
 */
@SpringBootApplication
@Slf4j
public class OrdersDispatchApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(OrdersDispatchApplication.class)
                .build(args)
                .run(args);
        log.info("家政服务-派单微服务启动");
    }
}
