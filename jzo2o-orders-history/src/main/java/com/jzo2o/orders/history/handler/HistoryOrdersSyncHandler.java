package com.jzo2o.orders.history.handler;

import com.jzo2o.canal.listeners.AbstractCanalRabbitMqMsgListener;
import com.jzo2o.orders.history.model.domain.HistoryOrdersSync;
import com.jzo2o.orders.history.service.IHistoryOrdersSyncService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class HistoryOrdersSyncHandler extends AbstractCanalRabbitMqMsgListener<HistoryOrdersSync> {

    @Resource
    private IHistoryOrdersSyncService historyOrdersSyncService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "canal-mq-jzo2o-orders-history", durable = "true"),
            exchange = @Exchange(name = "exchange.canal-jzo2o", type = ExchangeTypes.TOPIC),
            key = "canal-mq-jzo2o-orders-history"),
            concurrency = "1"
    )
    public void onMessage(Message message) throws Exception {
        parseMsg(message);
    }

    @Override
    public void batchSave(List<HistoryOrdersSync> historyOrdersSyncs) {
        historyOrdersSyncService.saveOrUpdateBatch(historyOrdersSyncs);
    }

    @Override
    public void batchDelete(List<Long> ids) {

    }
}
