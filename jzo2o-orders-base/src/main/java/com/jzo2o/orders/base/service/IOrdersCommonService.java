package com.jzo2o.orders.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.dto.OrderUpdateStatusDTO;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
public interface IOrdersCommonService extends IService<Orders> {

    Integer updateStatus(OrderUpdateStatusDTO orderUpdateStatusReqDTO);
}
