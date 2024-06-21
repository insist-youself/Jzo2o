package com.jzo2o.orders.history.service;

import com.jzo2o.orders.history.model.domain.HistoryOrdersSync;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.history.model.domain.StatDay;
import com.jzo2o.orders.history.model.domain.StatHour;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 历史订单完成15天后同步到历史订单同步表中，通过canal同步到历史订单库中；1天后删除（删除条件当天数据和历史订单库中的订单数据数量一致） 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-21
 */
public interface IHistoryOrdersSyncService extends IService<HistoryOrdersSync> {

    List<StatDay> statForDay(Integer statDay);

    List<StatHour> statForHour(Integer statDay);

    /**
     * 根据时间段统计总数
     * @param minSortTime
     * @param maxSortTime
     * @return
     */
    Integer countBySortTime(LocalDateTime minSortTime, LocalDateTime maxSortTime);
    /**
     * 根据时间段删除数据
     */
    void deleteBySortTime(LocalDateTime minSortTime, LocalDateTime maxSortTime);
}
