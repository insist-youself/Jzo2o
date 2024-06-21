package com.jzo2o.orders.seize.handler;

import com.jzo2o.canal.listeners.AbstractCanalRabbitMqMsgListener;
import com.jzo2o.common.model.Location;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.orders.base.constants.RedisConstants;
import com.jzo2o.orders.base.model.domain.OrdersSeize;
import com.jzo2o.orders.base.utils.RedisUtils;
import com.jzo2o.orders.seize.model.domain.OrdersSeizeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.jzo2o.orders.base.constants.EsIndexConstants.ORDERS_SEIZE;

/**
 * 抢单池同步类
 */
@Component
@Slf4j
public class OrdersSeizeSyncHandler extends AbstractCanalRabbitMqMsgListener<OrdersSeize> {

    @Resource
    private ElasticSearchTemplate elasticSearchTemplate;

    @Resource
    private RedisTemplate redisTemplate;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "canal-mq-jzo2o-orders-seize"),
            exchange = @Exchange(name = "exchange.canal-jzo2o", type = ExchangeTypes.TOPIC),
            key = "canal-mq-jzo2o-orders-seize"),
            concurrency = "1"
    )
    public void onMessage(Message message) throws Exception {
        parseMsg(message);
    }

    @Override
    public void batchSave(List<OrdersSeize> ordersSeizes) {
        // 1.es中添加抢单信息
        List<OrdersSeizeInfo> ordersSeizeInfos = ordersSeizes.stream().map(ordersSeize -> {
            OrdersSeizeInfo ordersSeizeInfo = BeanUtils.toBean(ordersSeize, OrdersSeizeInfo.class);
            //得到服务开始时间(yyMMddHH)
            String serveTimeString = DateTimeFormatter.ofPattern("yyMMddHH").format(ordersSeize.getServeStartTime());
            ordersSeizeInfo.setServeTime(Integer.parseInt(serveTimeString));
            ordersSeizeInfo.setLocation(new Location(ordersSeize.getLon(), ordersSeize.getLat()));
            ordersSeizeInfo.setKeyWords(ordersSeize.getServeTypeName() + ordersSeize.getServeItemName() + ordersSeize.getServeAddress());
            return ordersSeizeInfo;
        }).collect(Collectors.toList());

        Boolean result = elasticSearchTemplate.opsForDoc().batchInsert(ORDERS_SEIZE, ordersSeizeInfos);
        if (!result){
            throw new RuntimeException("同步抢单池加入es失败");
        }
        // 2.写入库存
        ordersSeizeInfos.stream().forEach(ordersSeizeInfo -> {
            String redisKey = String.format(RedisConstants.RedisKey.ORDERS_RESOURCE_STOCK, RedisUtils.getCityIndex(ordersSeizeInfo.getCityCode()));
            // 库存默认1
            redisTemplate.opsForHash().putIfAbsent(redisKey, ordersSeizeInfo.getId(), 1);
        });
    }

    @Override
    public void batchDelete(List<Long> ids) {
        log.info("抢单删除开始，删除数量:{},开始id：{}，结束id:{}", CollUtils.size(ids), CollUtils.getFirst(ids), CollUtils.getLast(ids));
        Boolean result = elasticSearchTemplate.opsForDoc().batchDelete(ORDERS_SEIZE, ids);
        if (!result){
            throw new RuntimeException("同步抢单池加入es失败");
        }
        log.info("抢单删除结束，删除数量:{},开始id：{}，结束id:{}", CollUtils.size(ids), CollUtils.getFirst(ids), CollUtils.getLast(ids));

    }
}
