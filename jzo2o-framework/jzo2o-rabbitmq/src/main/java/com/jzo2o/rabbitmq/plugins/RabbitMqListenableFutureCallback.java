package com.jzo2o.rabbitmq.plugins;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.rabbitmq.dao.FailMsgDao;
import lombok.Builder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author itcast
 */
@Builder
public class RabbitMqListenableFutureCallback implements ListenableFutureCallback<CorrelationData.Confirm> {

    //记录失败消息service
    private FailMsgDao failMsgDao;

    private String exchange;
    private String routingKey;
    private String msg;
    private Long msgId;
    private Integer delay;

    //是否是失败消息
    private boolean isFailMsg=false;

    @Override
    public void onFailure(Throwable ex) {
        if(failMsgDao == null) {
            return;
        }
        failMsgDao.save(msgId, exchange, routingKey, msg, delay, DateUtils.getCurrentTime() + 10, ExceptionUtil.getMessage(ex));
    }

    @Override
    public void onSuccess(CorrelationData.Confirm result) {
        if(failMsgDao == null){
            return;
        }
        if(!result.isAck()){
            // 执行失败保存失败信息，如果已经存在保存信息，如果不在信息信息
            failMsgDao.save(msgId, exchange, routingKey, msg, delay,DateUtils.getCurrentTime() + 10, "MQ回复nack");
        }else if(isFailMsg && msgId != null){
            // 如果发送的是失败消息，当收到ack需要从fail_msg删除该消息
            failMsgDao.removeById(msgId);
        }
    }
}
