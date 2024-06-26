package com.jzo2o.rabbitmq.plugins;

import com.jzo2o.common.utils.NumberUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * 延迟消息处理器
 *
 * @author itcast
 */
public class DelayMessagePostProcessor implements MessagePostProcessor {

    // 延迟队列默认延迟5s
    private static final int DEFAULT_DELAY = 5;
    private Integer delay;

    public DelayMessagePostProcessor(Integer delay) {
        this.delay = delay;
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {

        message.getMessageProperties().setDelay(NumberUtils.null2Default(delay, DEFAULT_DELAY));
        return message;
    }
}
