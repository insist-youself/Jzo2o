package com.jzo2o.orders.history.service;

import com.jzo2o.orders.history.model.dto.response.OperationHomePageResDTO;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author itcast
 */
public interface OrdersStatisticsService {
    /**
     * 运营端首页数据
     *
     * @param minTime 开始时间
     * @param maxTime 结束时间
     * @return 首页数据
     */
    OperationHomePageResDTO homePage(LocalDateTime minTime, LocalDateTime maxTime);

    /**
     * 导出统计数据
     *
     * @param minTime 开始时间
     * @param maxTime 结束时间
     */
    void downloadStatistics(LocalDateTime minTime, LocalDateTime maxTime) throws IOException;
}
