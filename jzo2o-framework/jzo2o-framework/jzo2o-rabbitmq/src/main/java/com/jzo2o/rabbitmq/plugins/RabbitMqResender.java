package com.jzo2o.rabbitmq.plugins;

import com.jzo2o.common.utils.IoUtils;
import com.jzo2o.common.utils.JsonUtils;
import com.jzo2o.rabbitmq.domain.ErrorRabbitMqMessage;
import com.jzo2o.rabbitmq.properties.RabbitmqProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class RabbitMqResender {

    private RabbitTemplate rabbitTemplate;
    private RabbitmqProperties rabbitmqProperties;
    private Channel channel;

    public RabbitMqResender(RabbitTemplate rabbitTemplate, RabbitmqProperties rabbitmqProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitmqProperties = rabbitmqProperties;
        channel = rabbitTemplate.getConnectionFactory()
                .createConnection()
                .createChannel(false);

    }

    /**
     * 从队列中获取一条数据并处理,如果没有消息，返回false，有消息返回true
     */
    public boolean getOneMessageAndProcess() {

        try {
            GetResponse response = channel.basicGet(rabbitmqProperties.getError().getQueue(), false);
            if(response == null) {
                return false;
            }
            ErrorRabbitMqMessage errorRabbitMqMessage = JsonUtils.toBean(new String(response.getBody()), ErrorRabbitMqMessage.class);
            Message message = MessageBuilder.withBody(errorRabbitMqMessage.getMessage().getBytes(Charset.defaultCharset())).build();
            rabbitTemplate.send(errorRabbitMqMessage.getOriginExchange(), errorRabbitMqMessage.getOriginRoutingKey(), message);
            channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
            return true;
        }catch (IOException e) {
            log.error("消息重发失败，e:",e);
            return false;
        }
    }

    @PreDestroy
    public void destory() {
        log.info("rabbitmq销毁...");
        IoUtils.close(channel);

    }
}
