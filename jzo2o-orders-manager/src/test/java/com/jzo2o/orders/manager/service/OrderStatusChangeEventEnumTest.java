//package com.jzo2o.orders.manager.service;
//
//import com.jzo2o.orders.base.enums.OrderStatusEnum;
//import com.jzo2o.statemachine.core.StatusChangeEvent;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
///**
// * @author itcast
// */
//@Getter
//@AllArgsConstructor
//public enum OrderStatusChangeEventEnumTest implements StatusChangeEvent {
//    PAYED_TEST(OrderStatusEnum.NO_PAY, OrderStatusEnum.DISPATCHING, "支付成功测试", "payed_test");
//
//    /**
//     * 源状态
//     */
//    private final OrderStatusEnum sourceStatus;
//
//    /**
//     * 目标状态
//     */
//    private final OrderStatusEnum targetStatus;
//
//    /**
//     * 描述
//     */
//    private final String desc;
//
//    /**
//     * 代码
//     */
//    private final String code;
//}