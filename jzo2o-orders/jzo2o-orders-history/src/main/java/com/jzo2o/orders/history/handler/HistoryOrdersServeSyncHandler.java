package com.jzo2o.orders.history.handler;

import com.jzo2o.canal.listeners.AbstractCanalRabbitMqMsgListener;
import com.jzo2o.orders.history.model.domain.HistoryOrders;
import com.jzo2o.orders.history.model.domain.HistoryOrdersServe;
import com.jzo2o.orders.history.model.domain.HistoryOrdersServeSync;
import com.jzo2o.orders.history.service.IHistoryOrdersServeService;
import com.jzo2o.orders.history.service.IHistoryOrdersServeSyncService;
import com.jzo2o.orders.history.service.IHistoryOrdersService;
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
public class HistoryOrdersServeSyncHandler extends AbstractCanalRabbitMqMsgListener<HistoryOrdersServeSync> {

    @Resource
    private IHistoryOrdersServeSyncService historyOrdersServeSyncService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "canal-mq-jzo2o-orders-serve-history",durable = "true"),
            exchange = @Exchange(name = "exchange.canal-jzo2o", type = ExchangeTypes.TOPIC),
            key = "canal-mq-jzo2o-orders-serve-history"),
            concurrency = "1"
    )
    public void onMessage(Message message) throws Exception {
        parseMsg(message);
    }

    @Override
    public void batchSave(List<HistoryOrdersServeSync> historyOrdersServeSyncs) {
        historyOrdersServeSyncService.saveOrUpdateBatch(historyOrdersServeSyncs);
    }

    @Override
    public void batchDelete(List<Long> ids) {

    }
}
