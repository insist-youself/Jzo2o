package com.jzo2o.foundations.handler;

import com.jzo2o.canal.listeners.AbstractCanalRabbitMqMsgListener;
import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.foundations.constants.IndexConstants;
import com.jzo2o.foundations.model.domain.ServeSync;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 86188
 */
@Component
public class ServeCanalDataSyncHandler extends AbstractCanalRabbitMqMsgListener<ServeSync> {

    @Resource
    private ElasticSearchTemplate elasticSearchTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "canal-mq-jzo2o-foundations"),
            exchange = @Exchange(name = "exchange.canal-jzo2o", type = ExchangeTypes.TOPIC),
            key = "canal-mq-jzo2o-foundations"),
            concurrency = "1"
    )
    public void onMessage(Message message) throws Exception {
        parseMsg(message);
    }

    @Override
    public void batchSave(List<ServeSync> data) {
        Boolean aBoolean = elasticSearchTemplate.opsForDoc().batchInsert(IndexConstants.SERVE, data);
        if(!aBoolean){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("同步失败");
        }
    }

    @Override
    public void batchDelete(List<Long> ids) {
        Boolean aBoolean = elasticSearchTemplate.opsForDoc().batchDelete(IndexConstants.SERVE, ids);

    }
}
