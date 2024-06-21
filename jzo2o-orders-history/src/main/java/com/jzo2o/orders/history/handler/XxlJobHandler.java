package com.jzo2o.orders.history.handler;

import com.jzo2o.orders.history.service.IHistoryOrdersServeService;
import com.jzo2o.orders.history.service.IHistoryOrdersService;
import com.jzo2o.orders.history.service.IStatDayService;
import com.jzo2o.orders.history.service.IStatHourService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class XxlJobHandler {

    @Resource
    private IStatDayService statDayService;

    @Resource
    private IStatHourService statHourService;

    @Resource
    private IHistoryOrdersService historyOrdersService;

    @Resource
    private IHistoryOrdersServeService historyOrdersServeService;

    /**
     * 按天统计保存15天内的订单数据
     * 按小时统计保存15天内的订单数据
     */
    @XxlJob("statAndSaveData")
    public void statAndSaveDataForDay() {
        //按天统计保存15天内的订单数据
        statDayService.statAndSaveData();
        //按小时统计保存15天内的订单数据
        statHourService.statAndSaveData();
    }
//    @XxlJob("statAndSaveDataForDay")
//    public void statAndSaveDataForDay() {
//        statDayService.statAndSaveData();
//    }

//    /**
//     * 按小时统计保存15天内的订单数据
//     */
//    @XxlJob("statAndSaveDataForHour")
//    public void statAndSaveDataForHour() {
//        statHourService.statAndSaveData();
//    }

    /**
     * 迁移HistoryOrdersSync同步表的数据到HistoryOrders历史订单表
     * 迁移HistoryOrdersServeSync同步表的数据到HistoryOrdersServe历史订单表
     * 规则：
     * 每天凌晨执行，迁移截止到昨日已完成15天的订单到历史订单
     *
     */
    @XxlJob("migrateHistoryOrders")
    public void migrateHistoryOrders(){
        //迁移HistoryOrdersSync同步表的数据到HistoryOrders历史订单表
        historyOrdersService.migrate();
        //删除迁移完成的数据
        historyOrdersService.deleteMigrated();


        //迁移HistoryOrdersServeSync同步表的数据到HistoryOrdersServe历史订单表
        historyOrdersServeService.migrate();
        //删除迁移完成的数据
        historyOrdersServeService.deleteMigrated();
    }

//    /**
//     * 迁移HistoryOrdersServeSync同步表的数据到HistoryOrdersServe历史订单表
//     * 规则：
//     * 每天凌晨执行，迁移截止到昨日已完成15天的服务单到历史服务单
//     */
//    @XxlJob("migrateHistoryOrdersServe")
//    public void migrateHistoryOrdersServe(){
//        historyOrdersServeService.migrate();
//        //删除迁移完成的数据
//        historyOrdersServeService.deleteMigrated();
//    }



//    /**
//     * 删除昨日已完结同步的订单，如果未完成数据迁移，无法删除
//     */
//    @XxlJob("deleteMigratedOrders")
//    public void deleteMigratedOrders(){
//        historyOrdersService.deleteMigrated();
//    }
//
//    /**
//     * 删除昨日已完结同步的服务单，如果未完成数据迁移，无法删除
//     */
//    @XxlJob("deleteMigratedOrdersServe")
//    public void deleteMigratedOrdersServe() {
//        historyOrdersServeService.deleteMigrated();
//    }
}
