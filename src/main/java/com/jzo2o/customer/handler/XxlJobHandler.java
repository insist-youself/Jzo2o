//package com.jzo2o.customer.handler;
//
//import com.jzo2o.rabbitmq.plugins.ErrorMessageRecoverer;
//import com.jzo2o.rabbitmq.plugins.RabbitMqResender;
//import com.xxl.job.core.handler.annotation.XxlJob;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * xxl-job定时任务
// */
//@Component
//@Slf4j
//public class XxlJobHandler {
//
//    @Resource
//    private RabbitMqResender rabbitMqResender;
//
//    /**
//     * rabbitmq异常消息拉取并重新发回队列
//     */
//    @XxlJob("rabbitmqErrorMsgPullAndResend")
//    public void rabbitmqErrorMsgPullAndResend(){
//
//        log.debug("rabbitmq异常消息重新");
//        for (int count = 0; count < 100; count++) {
//            try {
//                if(!rabbitMqResender.getOneMessageAndProcess()) {
//                    break;
//                }
//            }catch (Exception e){
//                log.error("rabbitmq异常消息拉取失败,e:",e);
//            }
//        }
//    }
//
//}
