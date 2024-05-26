package com.jzo2o.orders.manager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单取消模型
 *
 * @author itcast
 * @create 2023/8/7 17:12
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelDTO {
    /**
     * 订单id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 当前用户id
     */
    private Long currentUserId;

    /**
     * 当前用户名称
     */
    private String currentUserName;

    /**
     * 当前用户类型
     */
    private Integer currentUserType;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 预约服务开始时间
     */
    private LocalDateTime serveStartTime;

    /**
     * 实际支付金额
     */
    private BigDecimal realPayAmount;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 支付服务交易单号
     */
    private Long tradingOrderNo;
}
