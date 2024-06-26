package com.jzo2o.orders.history.service;

import com.jzo2o.orders.history.model.domain.HistoryOrdersServeSync;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务单 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-21
 */
public interface IHistoryOrdersServeSyncService extends IService<HistoryOrdersServeSync> {

    /**
     * 根据日期统计数量
     *
     * @param minSortTime 最小排序时间
     * @param maxSortTime 最大排序时间
     * @return 历史服务单数量
     */
    Integer countBySortTime(LocalDateTime minSortTime, LocalDateTime maxSortTime);

    void deleteBySortTime(LocalDateTime minSortTime, LocalDateTime maxSortTime);
}
