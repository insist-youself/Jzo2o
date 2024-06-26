package com.jzo2o.orders.base.handler;

import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.orders.base.enums.OrderPayStatusEnum;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.model.dto.OrderSnapshotDTO;
import com.jzo2o.orders.base.model.dto.OrderUpdateStatusDTO;
import com.jzo2o.orders.base.service.IOrdersCommonService;
import com.jzo2o.statemachine.core.StatusChangeEvent;
import com.jzo2o.statemachine.core.StatusChangeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.context.Theme;

import javax.annotation.Resource;

/**
 * 支付成功执行的动作
 * @author linger
 * @date 2024/6/12 20:11
 */
@Component("order_payed")
@Slf4j
public class OrderPayedHandler implements StatusChangeHandler<OrderSnapshotDTO> {

    @Resource
    private IOrdersCommonService ordersCommonService;

    @Override
    public void handler(String bizId, StatusChangeEvent statusChangeEventEnum, OrderSnapshotDTO bizSnapshot) {
        log.info("支付成功事件发布执行此动作....");
        // 统一对订单状态进行更新, 将订单状态由待支付改为已支付
        OrderUpdateStatusDTO orderUpdateStatusDTO = new OrderUpdateStatusDTO();
        orderUpdateStatusDTO.setId(Long.valueOf(bizId)); // 订单id
        orderUpdateStatusDTO.setOriginStatus(OrderStatusEnum.NO_PAY.getStatus());// 原始状态待支付
        orderUpdateStatusDTO.setTargetStatus(OrderStatusEnum.DISPATCHING.getStatus()); // 目标状态派单中
        orderUpdateStatusDTO.setPayStatus(OrderPayStatusEnum.PAY_SUCCESS.getStatus()); // 支付成功
        orderUpdateStatusDTO.setTradingOrderNo(bizSnapshot.getTradingOrderNo()); // 交易单号
        orderUpdateStatusDTO.setTradingChannel(bizSnapshot.getTradingChannel()); // 支付渠道
        orderUpdateStatusDTO.setTransactionId(bizSnapshot.getThirdOrderId()); // 第三方支付平台交易单号
        orderUpdateStatusDTO.setPayTime(bizSnapshot.getPayTime()); // 支付时间

        Integer integer = ordersCommonService.updateStatus(orderUpdateStatusDTO);
        if(integer < 1) {
            throw new CommonException("订单id " + bizSnapshot.getId() + " 支付成功动作执行失败");
        }
    }
}
