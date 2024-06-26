package com.jzo2o.rabbitmq.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorRabbitMqMessage implements Serializable {
    private String originRoutingKey;
    private String originExchange;
    private String message;
}
