package com.jzo2o.orders.base.config;

import com.jzo2o.orders.base.properties.DispatchProperties;
import com.jzo2o.orders.base.properties.ExecutorProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"com.jzo2o.orders.base.service","com.jzo2o.orders.base.handler"})
@MapperScan("com.jzo2o.orders.base.mapper")
//@Import({OrderStateMachine.class})
@EnableConfigurationProperties({DispatchProperties.class, ExecutorProperties.class})
public class AutoImportConfiguration {
}
