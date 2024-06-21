package com.jzo2o.orders.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.base.model.domain.HistoryOrdersSync;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersServe;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface IHistoryOrdersSyncService extends IService<HistoryOrdersSync> {


//    /**
//     * 查询已经完成的订单，15天前当天的历史订单
//     *
//     * @return
//     */
//    List<Orders> queryFinishedOrders(Long  id);
//
//    /**
//     * 批量组装历史订单写入历史订单同步表
//     *
//     * @param ordersList 即将进入历史订单同步表的订单
//     */
//    void batchWriteHistory(List<Orders> ordersList);

//    /**
//     * 写入订单历史同步表
//     * @param orderId 订单信息
//     */
//    void writeHistorySync(Long orderId);

//    /**
//     * 开启历史服务单同步记录处理任务
//     */
//    void startProcessHistoryOrdersTask();


//    void processHistoryOrders(Collection<Long> syncedIds, Collection<Long> noSyncedIds);

    /**
     * 删除已经完成的订单同步数据
     */
    void deleteFinished();
}
