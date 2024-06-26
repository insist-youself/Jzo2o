//package com.jzo2o.customer.listener;
//
//import cn.hutool.core.util.NumberUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.json.JSONUtil;
//import com.jzo2o.common.constants.MqConstants;
//import com.jzo2o.customer.model.dto.ScoreStatisticsMsg;
//import com.jzo2o.customer.properties.EvaluationProperties;
//import com.jzo2o.customer.service.IServeProviderService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * 监听mq消息
// *
// * @author itcast
// **/
//@Slf4j
//@Component
//public class EvaluationListener {
//    @Resource
//    private EvaluationProperties evaluationProperties;
//    @Resource
//    private IServeProviderService serveProviderService;
//
//    /**
//     *
//     * @param msg 消息
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = MqConstants.Queues.SCORE_STATISTICS),
//            exchange = @Exchange(name = MqConstants.Exchanges.EVALUATION_SCORE, type = ExchangeTypes.TOPIC),
//            key = MqConstants.RoutingKeys.SCORE_STATISTICS
//    ))
//    public void listenTradeUpdatePayStatusMsg(String msg) {
//        log.info("接收到评价系统评分同步的消息 ({})-> {}", MqConstants.Queues.SCORE_STATISTICS, msg);
//        ScoreStatisticsMsg scoreStatisticsMsg = JSONUtil.toBean(msg, ScoreStatisticsMsg.class);
//
//        //如果是服务项评分数据，直接跳过
//        if (ObjectUtil.equal(scoreStatisticsMsg.getTargetTypeId(), evaluationProperties.getServeItem().getTargetTypeId())) {
//            return;
//        }
//
//        Long scoreCount = scoreStatisticsMsg.getScoreCount();
//        Long goodLevelCount = scoreStatisticsMsg.getGoodLevelCount();
//
//        //综合评分
//        Double score = (null == scoreCount) ? 5.0 : NumberUtil.round(scoreStatisticsMsg.getTotalScore() * 1.0 / scoreCount, 1).doubleValue();
//
//        //好评率
//        String goodLevelRate =(null == scoreCount) ? "0.0%" :  NumberUtil.decimalFormat("#.##%", NumberUtil.div(goodLevelCount, scoreCount, 4));
//
//        //更新分数
//        serveProviderService.updateScoreById(Long.valueOf(scoreStatisticsMsg.getTargetId()), score, goodLevelRate);
//    }
//}
