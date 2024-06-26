package com.jzo2o.orders.history.model.dto.excel;

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

/**
 * @author itcast
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthElement {

    /**
     * 月份
     */
    @HeadStyle(hidden = BooleanEnum.TRUE)
    @ContentFontStyle(fontName = "宋体", fontHeightInPoints = 13, bold = BooleanEnum.TRUE, color = 30)
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ExcelProperty(value = "月份", index = 0)
    private String month;
}