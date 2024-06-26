package com.jzo2o.rabbitmq.client;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.jzo2o.common.expcetions.MqException;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.JsonUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.rabbitmq.dao.FailMsgDao;
import com.jzo2o.rabbitmq.plugins.DelayMessagePostProcessor;
import com.jzo2o.rabbitmq.plugins.RabbitMqListenableFutureCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 消息处理类
 *
 * @author zzj
 * @version 1.0
 */
@Slf4j
@Service
public class RabbitClient {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Autowired(required = false)
    private FailMsgDao failMsgDao;

    @Resource
    private RabbitClient rabbitClient;


    public void sendMsg(String exchange, String routingKey, Object msg) {
        rabbitClient.sendMsg(exchange, routingKey, msg, null, null, false);
    }

    /**
     * 发送消息 重试3次
     *
     * @param exchange   交换机
     * @param routingKey 路由key
     * @param msg        消息对象，会将对象序列化成json字符串发出
     * @param delay      延迟时间 秒
     * @param msgId      消息id
     * @param isFailMsg  是否是失败消息
     * @return 是否发送成功
     */
    @Retryable(value = MqException.class, maxAttempts = 3, backoff = @Backoff(value = 3000, multiplier = 1.5), recover = "saveFailMag")
    public void sendMsg(String exchange, String routingKey, Object msg, Integer delay, Long msgId, boolean isFailMsg) {
        // 1.发送消息前准备
        // 1.1获取消息内容，如果非字符串将其序列化
        String jsonMsg = JsonUtils.toJsonStr(msg);
        // 1.2.全局唯一消息id，如果调用者设置了消息id，使用调用者消息id，如果为配置，默认雪花算法生成消息id
        msgId = NumberUtils.null2Default(msgId, IdUtil.getSnowflakeNextId());
        // 1.3.设置默认延迟时间，默认立即发送
        delay = NumberUtils.null2Default(delay, -1);
        log.debug("消息发送！exchange = {}, routingKey = {}, msg = {}, msgId = {}", exchange, routingKey, jsonMsg, msgId);


        // 1.4.构建回调
        RabbitMqListenableFutureCallback futureCallback = RabbitMqListenableFutureCallback.builder()
                .exchange(exchange)
                .routingKey(routingKey)
                .msg(jsonMsg)
                .msgId(msgId)
                .delay(delay)
                .isFailMsg(isFailMsg)
                .failMsgDao(failMsgDao)
                .build();
        // 1.5.CorrelationData设置
        CorrelationData correlationData = new CorrelationData(msgId.toString());
        correlationData.getFuture().addCallback(futureCallback);

        // 1.6.构造消息对象
        Message message = MessageBuilder.withBody(StrUtil.bytes(jsonMsg, CharsetUtil.CHARSET_UTF_8))
                //持久化
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                //消息id
                .setMessageId(msgId.toString())
                .build();

        try {
            // 2.发送消息
            this.rabbitTemplate.convertAndSend(exchange, routingKey, message, new DelayMessagePostProcessor(delay), correlationData);
        } catch (Exception e) {
            log.error("send error:" + e);
            // 3.构建异常回调，并抛出异常
            MqException mqException = new MqException();
            mqException.setMsg(ExceptionUtil.getMessage(e));
            mqException.setMqId(msgId);
            throw mqException;
        }
    }


    /**
     * @param mqException mq异常消息
     * @param exchange    交换机
     * @param routingKey  路由key
     * @param msg         mq消息
     * @param delay       延迟消息
     * @param msgId       消息id
     */
    @Recover
    public void saveFailMag(MqException mqException, String exchange, String routingKey, Object msg, Integer delay, String msgId) {
        //发送消息失败，需要将消息持久化到数据库，通过任务调度的方式处理失败的消息
        failMsgDao.save(mqException.getMqId(), exchange, routingKey, JsonUtils.toJsonStr(msg), delay, DateUtils.getCurrentTime() + 10,  ExceptionUtil.getMessage(mqException));
    }


}
