package com.jzo2o.rabbitmq.config;

import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.rabbitmq.client.RabbitClient;
import com.jzo2o.rabbitmq.dao.FailMsgDao;
import com.jzo2o.rabbitmq.dao.impl.FailMsgDaoImpl;
import com.jzo2o.rabbitmq.plugins.ErrorMessageRecoverer;
import com.jzo2o.rabbitmq.plugins.RabbitMqResender;
import com.jzo2o.rabbitmq.properties.RabbitmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @author itcast
 */
@Configuration
@ConditionalOnProperty(prefix = "rabbit-mq", name = "enable", havingValue = "true")
@Import({RabbitClient.class, FailMsgDaoImpl.class})
@EnableConfigurationProperties({RabbitmqProperties.class})
@Slf4j
public class RabbitMqConfiguration implements ApplicationContextAware {


    /**
     * 并发数量
     */
    public static final int DEFAULT_CONCURRENT = 10;

    @Autowired(required = false)
    private FailMsgDao failMsgDao;

    @Bean
    public ErrorMessageRecoverer errorMessageRecoverer(RabbitmqProperties rabbitmqProperties,RabbitClient rabbitClient) {
        return new ErrorMessageRecoverer(rabbitClient, rabbitmqProperties);
    }

    @Bean
    public RabbitMqResender rabbitMqResender(RabbitTemplate rabbitTemplate, RabbitmqProperties rabbitmqProperties) {
        return new RabbitMqResender(rabbitTemplate, rabbitmqProperties);
    }
    /**
     * 自定义mq消费者并发连接
     *
     * @param configurer        configurer
     * @param connectionFactory connectionFactory
     * @return factory
     */
    @Bean("defaultContainerFactory")
    @Primary
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                                 ConnectionFactory connectionFactory,
                                                                 RabbitmqProperties rabbitmqProperties) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 声明消费异常交换机
        Exchange errorExchange = ExchangeBuilder.topicExchange(rabbitmqProperties.getError().getExchange())
                .durable(true).build();
        rabbitAdmin.declareExchange(errorExchange);
        rabbitAdmin.setAutoStartup(true);
        // 声明消费异常队列
        Queue errorQueue = new Queue(rabbitmqProperties.getError().getQueue());
        rabbitAdmin.declareQueue(errorQueue);
        // 声明消费异常消息绑定关系
        Binding binding = BindingBuilder.bind(errorQueue)
                .to(errorExchange)
                .with(rabbitmqProperties.getError().getRoutingKey()).noargs();
        rabbitAdmin.declareBinding(binding);
        return factory;
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        //定义returnCallback回调方法
        rabbitTemplate.setReturnsCallback(
                new RabbitTemplate.ReturnsCallback() {
                    @Override
                    public void returnedMessage(ReturnedMessage returnedMessage) {
                        byte[] body = returnedMessage.getMessage().getBody();
                        //消息id
                        String messageId = returnedMessage.getMessage().getMessageProperties().getMessageId();
                        String content = new String(body, Charset.defaultCharset());
                        log.info("消息发送失败，应答码{}，原因{}，交换机{}，路由键{},消息id{},消息内容{}",
                                returnedMessage.getReplyCode(),
                                returnedMessage.getReplyText(),
                                returnedMessage.getExchange(),
                                returnedMessage.getRoutingKey(),
                                messageId,
                                content);
                        if (failMsgDao != null) {
                            failMsgDao.save(NumberUtils.parseLong(messageId), returnedMessage.getExchange(), returnedMessage.getRoutingKey(), content, null, DateUtils.getCurrentTime(), "returnCallback");
                        }
                    }
                }
        );
    }

}
