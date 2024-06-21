package com.jzo2o.orders.dispatch.handler;

import com.jzo2o.canal.listeners.AbstractCanalRabbitMqMsgListener;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.orders.base.constants.RedisConstants;
import com.jzo2o.orders.base.model.domain.OrdersDispatch;
import com.jzo2o.orders.base.utils.RedisUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.DISPATCH_LIST;

/**
 * @author 86188
 */
@Component
public class OrdersDispatchSyncHandler extends AbstractCanalRabbitMqMsgListener<OrdersDispatch> {

    @Resource
    private RedisTemplate redisTemplate;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "canal-mq-jzo2o-orders-dispatch"),
            exchange = @Exchange(name = "exchange.canal-jzo2o", type = ExchangeTypes.TOPIC),
            key = "canal-mq-jzo2o-orders-dispatch"),
            concurrency = "1"
    )
    public void onMessage(Message message) throws Exception {
        parseMsg(message);
    }

    @Override
    public void batchSave(List<OrdersDispatch> data) {
        // 1.同步派单列表
        // 1.1.派单列表过滤（人工派单的不进入派单列表）
        //ZSetOperations.TypedTuple表示Sorted Set有序集合的元素，包括：value和分数
        Set<ZSetOperations.TypedTuple> ordersDispatchIdTypedTupleSet = data.stream()
                .filter(ordersDispatch -> ordersDispatch.getIsTransferManual() == 0 || DateUtils.now().compareTo(ordersDispatch.getServeStartTime()) < 0)
                .map(ordersDispatch -> ZSetOperations.TypedTuple.of(ordersDispatch.getId(), DateUtils.getCurrentTime() * 1d))
                .collect(Collectors.toSet());
        // 1.2.同步派单列表
        redisTemplate.opsForZSet().addIfAbsent(DISPATCH_LIST, ordersDispatchIdTypedTupleSet);

        // 2.同步库存，在抢单模块已同步此库存这里无需再同步
        // 2.1.库存过滤
//        data.stream()
//                .filter(ordersDispatch -> ordersDispatch.getIsTransferManual() == 0 || DateUtils.now().compareTo(ordersDispatch.getServeStartTime()) < 0)
//                .forEach(ordersDispatch -> {
//                    String redisKey = String.format(RedisConstants.RedisKey.ORDERS_RESOURCE_STOCK, RedisUtils.getCityIndex(ordersDispatch.getCityCode()));
//                    // 库存默认1
//                    redisTemplate.opsForHash().putIfAbsent(redisKey, ordersDispatch.getId(), 1);
//                });
    }

    @Override
    public void batchDelete(List<Long> ids) {
        // 清空派单列表
        redisTemplate.opsForZSet().remove(DISPATCH_LIST, ids);
    }

}
