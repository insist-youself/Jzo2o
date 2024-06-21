package com.jzo2o.orders.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.base.model.domain.HistoryOrdersSync;

import java.util.Collection;

public interface IHistoryOrdersSyncCommonService extends IService<HistoryOrdersSync> {

    /**
     * 写入订单历史同步表
     * @param orderId 订单信息
     */
    void writeHistorySync(Long orderId);

}
