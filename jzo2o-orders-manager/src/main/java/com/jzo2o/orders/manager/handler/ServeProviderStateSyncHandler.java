//package com.jzo2o.orders.manager.handler;
//
//import com.jzo2o.api.customer.ServeProviderApi;
//import com.jzo2o.canal.listeners.AbstractCanalRabbitMqMsgListener;
//import com.jzo2o.common.utils.BeanUtils;
//import com.jzo2o.es.core.ElasticSearchTemplate;
//import com.jzo2o.orders.base.model.domain.ServeProviderInfo;
//import com.jzo2o.orders.base.model.domain.ServeProviderSync;
//import com.jzo2o.orders.base.utils.RedisUtils;
//import com.jzo2o.orders.manager.constants.EsIndex;
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.*;
//
///**
// *
// * 服务提供者服务状态同步类
// * @author 86188
// */
//@Component
//public class ServeProviderStateSyncHandler extends AbstractCanalRabbitMqMsgListener<ServeProviderSync> {
//
//    @Resource
//    private ElasticSearchTemplate elasticSearchTemplate;
//
//    @Resource
//    private ServeProviderApi serveProviderApi;
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "canal-mq-jzo2o-orders-provider"),
//            exchange = @Exchange(name = "exchange.canal-jzo2o", type = ExchangeTypes.TOPIC),
//            key = "canal-mq-jzo2o-orders-provider"),
//            concurrency = "1"
//    )
//    public void onMessage(Message message) throws Exception {
//        parseMsg(message);
//    }
//
//    @Override
//    public void batchSave(List<ServeProviderSync> data) {
//        List<ServeProviderInfo> serveProviderInfos = BeanUtils.copyToList(data, ServeProviderInfo.class);
//        // 1.同步es
//        elasticSearchTemplate.opsForDoc().batchUpsert(EsIndex.SERVE_PROVIDER_INFO, serveProviderInfos);
//        // 2.同步redis
//        //服务提供者id
//        List<Long> serveProviderIds = data.stream().map(ServeProviderSync::getId).collect(Collectors.toList());
//        //获取服务提供者的城市编码
//        Map<Long, String> serveProviderIdAndCityCodeMap = serveProviderApi.batchCityCode(serveProviderIds);
//
//        data.stream().forEach(serveProviderSync -> {
//            //获取服务提供者的城市编码
//            String cityCode = serveProviderIdAndCityCodeMap.get(serveProviderSync.getId());
//            int index = RedisUtils.getCityIndex(cityCode);
//            // 服务时间状态redisKey
//            String serveProviderStateRedisKey = String.format(SERVE_PROVIDER_STATE, index);
//            Map<String, Object> map = new HashMap<>();
//            // 服务时间列表
//            map.put(serveProviderSync.getId() + "_times", serveProviderSync.getServeTimes());
//            // 服务数量
//            map.put(serveProviderSync.getId() + "_num", serveProviderSync.getAcceptanceNum());
//            //写入redis
//            redisTemplate.opsForHash().putAll(serveProviderStateRedisKey, map);
//        });
//    }
//
//    @Override
//    public void batchDelete(List<Long> ids) {
//    }
//}
