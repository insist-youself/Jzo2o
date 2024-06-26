//package com.jzo2o.customer.handler;
//
//import com.jzo2o.canal.listeners.AbstractCanalRabbitMqMsgListener;
//import com.jzo2o.common.expcetions.BadRequestException;
//import com.jzo2o.common.expcetions.CommonException;
//import com.jzo2o.common.model.Location;
//import com.jzo2o.common.utils.BeanUtils;
//import com.jzo2o.common.utils.CollUtils;
//import com.jzo2o.customer.constants.EsIndexConstants;
//import com.jzo2o.customer.model.domain.ServeProviderInfo;
//import com.jzo2o.customer.model.domain.ServeProviderSync;
//import com.jzo2o.es.core.ElasticSearchTemplate;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author 86188
// */
//@Data
//@Component
//@Slf4j
//public class ServeProviderHandler extends AbstractCanalRabbitMqMsgListener<ServeProviderSync> {
//    @Resource
//    private ElasticSearchTemplate elasticSearchTemplate;
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "canal-mq-jzo2o-customer-provider"),
//            exchange = @Exchange(name = "exchange.canal-jzo2o", type = ExchangeTypes.TOPIC),
//            key = "canal-mq-jzo2o-customer-provider"),
//            concurrency = "1"
//    )
//    public void onMessage(Message message) throws Exception {
//        parseMsg(message);
//    }
//
//    @Override
//    public void batchSave(List<ServeProviderSync> data) {
//        List<ServeProviderInfo> serveProviderInfos = BeanUtils.copyToList(data, ServeProviderInfo.class, (sync, info) -> {
//            info.setLocation(new Location(sync.getLon(), sync.getLat()));
//        });
//        log.debug("serveProviderInfos : {}", serveProviderInfos);
//
//        if(!elasticSearchTemplate.opsForDoc().batchUpsert(EsIndexConstants.SERVE_PROVIDER_INFO, serveProviderInfos)){
//            throw new CommonException("服务人员或机构信息同步异常");
//        }
//    }
//
//    @Override
//    public void batchDelete(List<Long> ids) {
//        elasticSearchTemplate.opsForDoc().batchDelete(EsIndexConstants.SERVE_PROVIDER_INFO, ids);
//    }
//}
