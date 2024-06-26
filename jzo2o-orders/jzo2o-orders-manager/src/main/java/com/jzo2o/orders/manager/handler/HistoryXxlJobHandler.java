package com.jzo2o.orders.manager.handler;

import com.jzo2o.orders.manager.service.IHistoryOrdersServeSyncService;
import com.jzo2o.orders.manager.service.IHistoryOrdersSyncService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 历史订单相关任务
 * 任务1：将订单完成、取消、关闭超过15天的订单同步到历史订单同步表中
 * 任务2：删除已经进入历史订单的同步记录
 */
@Component
@Slf4j
public class HistoryXxlJobHandler {


    @Resource
    private IHistoryOrdersSyncService historyOrdersSyncService;

    @Resource
    private IHistoryOrdersServeSyncService historyOrdersServeSyncService;

    /**
     * 删除已经完成的服务单
     */
    @XxlJob("deleteFinishedOrders")
    public void deleteFinishedOrders() {
        historyOrdersSyncService.deleteFinished();
    }

    /**
     * 删除已经完结的服务单
     */
    @XxlJob("deleteFinishedOrdersServe")
    public void deleteFinishedOrdersServe() {
        historyOrdersServeSyncService.deleteFinished();
    }



}
