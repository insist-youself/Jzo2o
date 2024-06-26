package com.jzo2o.orders.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.base.model.domain.OrdersRefund;

import java.util.List;

/**
 * <p>
 * 订单退款表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-07
 */
public interface IOrdersRefundService extends IService<OrdersRefund> {

    /**
     * 查询指定数量的退款订单
     *
     * @param count 数量
     */
    List<OrdersRefund> queryRefundOrderListByCount(Integer count);
}
