package com.jzo2o.orders.history.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 运营端首页数据模型
 *
 * @author itcast
 * @create 2023/9/21 15:21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationHomePageResDTO {
    /**
     * 有效订单数
     */
    private Integer effectiveOrderNum;

    /**
     * 取消订单数
     */
    private Integer cancelOrderNum;

    /**
     * 关闭订单数
     */
    private Integer closeOrderNum;

    /**
     * 有效总金额
     */
    private BigDecimal effectiveOrderTotalAmount;

    /**
     * 实付订单均价
     */
    private BigDecimal realPayAveragePrice;

    /**
     * 订单总数
     */
    private Integer totalOrderNum;

    /**
     * 订单数趋势
     */
    private List<OrdersCount> ordersTrend;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdersCount {
        /**
         * 时间
         */
        private String dateTime;

        /**
         * 数量
         */
        private Integer count;
    }

    /**
     * 默认属性
     *
     * @return 默认模型
     */
    public static OperationHomePageResDTO defaultInstance() {
        return OperationHomePageResDTO.builder()
                .effectiveOrderNum(0)
                .cancelOrderNum(0)
                .closeOrderNum(0)
                .effectiveOrderTotalAmount(BigDecimal.ZERO)
                .realPayAveragePrice(BigDecimal.ZERO)
                .totalOrderNum(0)
                .ordersTrend(Collections.emptyList())
                .build();
    }

    /**
     * 按小时统计订单数趋势默认值
     *
     * @return 订单数趋势
     */
    public static List<OperationHomePageResDTO.OrdersCount> defaultHourOrdersTrend() {
        List<OperationHomePageResDTO.OrdersCount> defaultOrdersTrend = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String dateTime = i < 10 ? "0" + i : String.valueOf(i);
            OperationHomePageResDTO.OrdersCount ordersCount = new OperationHomePageResDTO.OrdersCount(dateTime, 0);
            defaultOrdersTrend.add(ordersCount);
        }
        return defaultOrdersTrend;
    }

    /**
     * 按日统计订单数趋势默认值
     *
     * @param minDateTime 最小时间
     * @param maxDateTime 最大时间
     * @return 订单数趋势
     */
    public static List<OperationHomePageResDTO.OrdersCount> defaultDayOrdersTrend(LocalDateTime minDateTime, LocalDateTime maxDateTime) {
        List<OperationHomePageResDTO.OrdersCount> defaultOrdersTrend = new ArrayList<>();
        while (true) {
            //定义日期，格式：月.日，如：9.2
            int month = minDateTime.getMonthValue();
            int day = minDateTime.getDayOfMonth();
            String dateTime = month + "." + day;

            //组装订单数趋势数据
            OperationHomePageResDTO.OrdersCount ordersCount = new OperationHomePageResDTO.OrdersCount(dateTime, 0);
            defaultOrdersTrend.add(ordersCount);

            //最小时间自增，最小时间大于最大时间则循环结束
            minDateTime = minDateTime.plusDays(1);
            if (minDateTime.isAfter(maxDateTime)) {
                break;
            }
        }
        return defaultOrdersTrend;
    }
}
