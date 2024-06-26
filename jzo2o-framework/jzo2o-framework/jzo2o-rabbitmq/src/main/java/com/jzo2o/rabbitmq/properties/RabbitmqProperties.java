package com.jzo2o.rabbitmq.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "rabbit-mq.recoverer")
@Configuration
public class RabbitmqProperties {

    @NestedConfigurationProperty
    private Error error = new Error();

    @Data
    public static class Error {
        /**
         * mq消费异常接收routingKey
         */
        private String routingKey = "error.rounting-key";
        /**
         * mq消费异常接收交换机
         */
        private String exchange = "error.exchange";

        /**
         * mq消费异常接收队列
         */
        private String queue = "error.queue";
        /**
         * 消费异常白名单，进入白名单的routingKey才会进入异常队列重新处理
         */
        @NestedConfigurationProperty
        private List<String> whiteList = new ArrayList<>();
    }



}
