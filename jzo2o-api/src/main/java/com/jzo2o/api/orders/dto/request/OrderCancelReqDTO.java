package com.jzo2o.api.orders.dto.request;

import lombok.Data;

/**
 * 订单取消请求
 *
 * @author itcast
 * @create 2023/8/7 08:59
 **/
@Data
public class OrderCancelReqDTO {
    /**
     * 订单id
     */
    private Long id;

    /**
     * 取消/退款原因
     */
    private String cancelReason;
}
