package com.jzo2o.orders.history.model.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 聚合统计数据
 *
 * @author itcast
 * @create 2023/9/21 18:15
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 42, verticalAlignment = VerticalAlignmentEnum.CENTER, horizontalAlignment = HorizontalAlignmentEnum.CENTER)
@ContentFontStyle(fontName = "宋体", fontHeightInPoints = 12, bold = BooleanEnum.TRUE)
public class AggregationStatisticsData {

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
}
