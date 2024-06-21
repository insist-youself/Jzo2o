package com.jzo2o.orders.history.model.dto.excel;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计数据
 *
 * @author itcast
 * @create 2023/9/21 18:15
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ContentStyle(verticalAlignment = VerticalAlignmentEnum.CENTER, horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ContentFontStyle(fontName = "宋体", fontHeightInPoints = 11)
public class StatisticsData {

    /**
     * 统计日期，按小时统计格式：HH:mm:ss,按天统计格式：某月某日
     */
    @HeadStyle(hidden = BooleanEnum.TRUE)
    @ExcelProperty(value = "日期", index = 0)
    private String statTime;

    /**
     * 订单总数
     */
    @HeadStyle(hidden = BooleanEnum.TRUE)
    @ExcelProperty(value = "订单数", index = 1)
    private Integer totalOrderNum;

    /**
     * 有效订单数
     */
    @HeadStyle(hidden = BooleanEnum.TRUE)
    @ExcelProperty(value = "有效订单数", index = 2)
    private Integer effectiveOrderNum;

    /**
     * 取消订单数
     */
    @HeadStyle(hidden = BooleanEnum.TRUE)
    @ExcelProperty(value = "取消订单数", index = 3)
    private Integer cancelOrderNum;

    /**
     * 关闭订单数
     */
    @HeadStyle(hidden = BooleanEnum.TRUE)
    @ExcelProperty(value = "关闭订单数", index = 4)
    private Integer closeOrderNum;

    /**
     * 有效总金额
     */
    @HeadStyle(hidden = BooleanEnum.TRUE)
    @ExcelProperty(value = "有效总金额", index = 5)
    private BigDecimal effectiveOrderTotalAmount;

    /**
     * 实付订单均价
     */
    @HeadStyle(hidden = BooleanEnum.TRUE)
    @ExcelProperty(value = "实付订单均价", index = 6)
    private BigDecimal realPayAveragePrice;

    /**
     * 按小时统计默认数据
     *
     * @return 统计数据
     */
    public static List<StatisticsData> defaultHourStatisticsData() {
        List<StatisticsData> defaultStatisticsData = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String dateTime = i < 10 ? "0" + i + "时" : i + "时";
            StatisticsData statisticsData = new StatisticsData(dateTime, 0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO);
            defaultStatisticsData.add(statisticsData);
        }
        return defaultStatisticsData;
    }

    /**
     * 按日统计的默认数据
     *
     * @param minTime 开始时间
     * @param maxTime 结束时间
     * @return 统计数据
     */
    public static List<StatisticsData> defaultDayStatisticsData(LocalDateTime minTime, LocalDateTime maxTime) {
        List<StatisticsData> defaultStatisticsData = new ArrayList<>();
        while (true) {
            //定义日期，格式：某yyyyMMdd
            String dateTime = LocalDateTimeUtil.format(minTime, DatePattern.PURE_DATE_PATTERN);

            //组装按日统计数据
            StatisticsData statisticsData = new StatisticsData(dateTime, 0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO);
            defaultStatisticsData.add(statisticsData);

            //最小时间自增，最小时间大于最大时间则循环结束
            minTime = minTime.plusDays(1);
            if (minTime.isAfter(maxTime)) {
                break;
            }
        }
        return defaultStatisticsData;
    }
}
