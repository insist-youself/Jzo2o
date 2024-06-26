package com.jzo2o.orders.base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateStatusDTO {
    /**
     * 订单id
     */
    private Long id;

    /**
     * 原订单状态
     */
    private Integer originStatus;

    /**
     * 目标订单状态
     */
    private Integer targetStatus;

    /**
     * 支付状态
     */
    private Integer payStatus;
    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 评价时间
     */
    private LocalDateTime evaluationTime;

    /**
     * 支付服务交易单号
     */
    private Long tradingOrderNo;

    /**
     * 第三方支付的交易号
     */
    private String transactionId;

    /**
     * 支付渠道
     */
    private String tradingChannel;

    /**
     * 实际服务完成时间
     */
    private LocalDateTime realServeEndTime;

    /**
     * 评价状态
     */
    private Integer evaluationStatus;
}
