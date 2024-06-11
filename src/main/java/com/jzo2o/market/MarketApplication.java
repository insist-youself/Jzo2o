package com.jzo2o.market;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
@Slf4j
@MapperScan("com.jzo2o.market.mapper")
public class MarketApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MarketApplication.class)
                .build(args)
                .run(args);
        log.info("家政服务-营销中心启动");
    }
}
