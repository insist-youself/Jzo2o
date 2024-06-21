package com.jzo2o.orders.manager.service;

import com.jzo2o.orders.base.model.domain.HistoryOrdersServeSync;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.base.model.domain.HistoryOrdersSync;
import com.jzo2o.orders.base.model.domain.OrdersServe;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务任务 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-14
 */
public interface IHistoryOrdersServeSyncService extends IService<HistoryOrdersServeSync> {

//    /**
//     * 滚动查询已经完成的订单，15天前当天的历史服务单
//     *
//     * @return
//     */
//    List<OrdersServe> queryFinishedOrdersServe(Long lastId);
//
//    /**
//     * 批量组装历史服务单写入历史服务单同步表
//     *
//     * @param ordersServes 即将进入历史服务单同步表的服务单列表
//     */
//    void batchWriteHistory(List<OrdersServe> ordersServes);

//    /**
//     * 开启历史服务单同步记录处理任务
//     */
//    void startProcessHistoryOrdersServeTask();
//
//    /**
//     * 处理历史服务单信息，已经同步的历史服务单同步记录删除，未同步的历史服务单同步记录修改更新时间重新同步
//     * @param syncedIds 已经同步的历史服务单id列表
//     * @param noSyncedIds 未同步的历史服务单id列表
//     */
//    void processHistoryOrdersServe(List<Long> syncedIds, List<Long> noSyncedIds);

    /**
     * 删除已经完结的服务单
     */
    void deleteFinished();
}
