package com.jzo2o.orders.manager.service;

import com.jzo2o.orders.base.model.dto.OrderSnapshotDTO;
import com.jzo2o.statemachine.core.StatusChangeEvent;
import com.jzo2o.statemachine.core.StatusChangeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 未支付时订单取消处理器
 *
 * @author itcast
 * @create 2023/8/17 18:08
 **/
@Slf4j
@Component("order_payed_test")//bean名称规则为:状态机名称_状态变更事件名称
public class OrderStateHandlerTest implements StatusChangeHandler<OrderSnapshotDTO> {

    /**
     * 订单评价处理逻辑
     *
     * @param bizId   业务id
     * @param bizSnapshot 快照
     */
    @Override
    public void handler(String bizId, StatusChangeEvent statusChangeEventEnum, OrderSnapshotDTO bizSnapshot) {
        log.info("订单状态变更处理逻辑开始，订单号：{},状态变更前:{},变更后:{}", bizId,statusChangeEventEnum.getSourceStatus(),statusChangeEventEnum.getTargetStatus());
        //执行业务逻辑，更新订单表的订单状态为变更后的状态
        //...

    }
}
