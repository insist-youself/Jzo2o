package com.jzo2o.orders.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author itcast
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@Slf4j
@EnableTransactionManagement
public class OrdersManagerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(OrdersManagerApplication.class)
                .build(args)
                .run(args);
        log.info("家政服务-订单管理微服务启动");
    }
}
