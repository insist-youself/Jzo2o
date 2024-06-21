package com.jzo2o.orders.history.model.dto.excel;

import lombok.Data;

import java.util.List;

/**
 * 导出表格月度数据
 *
 * @author itcast
 * @create 2023/9/21 20:33
 **/
@Data
public class ExcelMonthData {
    /**
     * 月份
     */
    private String month;

    /**
     * 当月每日统计数据
     */
    private List<StatisticsData> statisticsDataList;

    /**
     * 按月聚合数据
     */
    private AggregationStatisticsData monthAggregation;
}
