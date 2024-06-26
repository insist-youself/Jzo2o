package com.jzo2o.trade;

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
@MapperScan("com.jzo2o.trade.mapper")
@SpringBootApplication
public class TradeApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(TradeApplication.class)
                .build(args)
                .run(args);
        log.info("家政服务-支付服务启动");
    }
}
