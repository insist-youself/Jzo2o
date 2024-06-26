package com.jzo2o.statemachine.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 状态机配置
 *
 * @author itcast
 **/
@ComponentScan(value = {"com.jzo2o.statemachine"})
public class StateConfiguration {

    /**
     * 用于状态机持久化的mapper初始化为bean
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.jzo2o.statemachine.mapper");
        return mapperScannerConfigurer;
    }
}
