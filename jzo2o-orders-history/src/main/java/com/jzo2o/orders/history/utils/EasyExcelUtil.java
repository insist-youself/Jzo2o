package com.jzo2o.orders.history.utils;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * easyexcel工具类
 */
public class EasyExcelUtil implements CellWriteHandler {
    /**
     * 月份所在行
     */
    private List<Integer> monthRowIndexList;

    /**
     * 聚合统计数据所在行
     */
    private List<Integer> aggregationRowIndexList;

    public EasyExcelUtil() {
    }


    public EasyExcelUtil(List<Integer> monthRowIndexList, List<Integer> aggregationRowIndexList) {
        this.monthRowIndexList = monthRowIndexList;
        this.aggregationRowIndexList = aggregationRowIndexList;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        Cell cell = context.getCell();
        Sheet sheet = context.getWriteSheetHolder().getSheet();

        //程序执行到聚合统计数据所在行，则将该单元格背景填充为浅绿色
        if (aggregationRowIndexList.contains(cell.getRowIndex())) {
            WriteCellData<?> cellData = context.getFirstCellData();
            // 这里需要去cellData 获取样式
            // 很重要的一个原因是 WriteCellStyle 和 dataFormatData绑定的 简单的说 比如你加了 DateTimeFormat
            // ，已经将writeCellStyle里面的dataFormatData 改了 如果你自己new了一个WriteCellStyle，可能注解的样式就失效了
            // 然后 getOrCreateStyle 用于返回一个样式，如果为空，则创建一个后返回
            WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
            writeCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
            writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

            Row row = sheet.getRow(cell.getRowIndex());
            row.setHeight((short) 440);
        }

        //程序执行到月份所在行
        if (monthRowIndexList.contains(cell.getRowIndex())) {
            //月份所在行需要合并单元格
            CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), 0, 6);
            sheet.addMergedRegion(cellRangeAddress);

            //设置月份所在行的行高，行高29（excel行高值）
            Row row = sheet.getRow(cell.getRowIndex());
            //height值=excel的行高值*20。例如，excel中设置行高为20，则该处的row.getHeight为400
            row.setHeight((short) 580);
        }
    }
}
