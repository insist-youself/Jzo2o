package com.jzo2o.orders.seize.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.base.model.domain.OrdersServe;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务任务 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
public interface IOrdersServeService extends IService<OrdersServe> {

    /**
     * 统计当前服务单数（包含待接单、待开始、待分配
     * @param serveProviderId
     * @return
     */
    List<Integer> countServeTimes(Long serveProviderId);

    /**
     * 统计当前订单未完成的服务数量
     * @param serveProviderId
     * @return
     */
    Integer countNoServedNum(Long serveProviderId);

    OrdersServe findById(Long id);

}
