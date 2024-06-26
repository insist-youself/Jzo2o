package com.jzo2o.orders.history.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.mvc.utils.ResponseUtils;
import com.jzo2o.orders.history.model.domain.StatDay;
import com.jzo2o.orders.history.model.domain.StatHour;
import com.jzo2o.orders.history.model.dto.excel.AggregationStatisticsData;
import com.jzo2o.orders.history.model.dto.excel.ExcelMonthData;
import com.jzo2o.orders.history.model.dto.excel.MonthElement;
import com.jzo2o.orders.history.model.dto.excel.StatisticsData;
import com.jzo2o.orders.history.model.dto.response.OperationHomePageResDTO;
import com.jzo2o.orders.history.service.IStatDayService;
import com.jzo2o.orders.history.service.IStatHourService;
import com.jzo2o.orders.history.service.OrdersStatisticsService;
import com.jzo2o.orders.history.utils.EasyExcelUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jzo2o.mvc.constants.HeaderConstants.BODY_PROCESSED;

/**
 * 订单统计服务层
 *
 * @author itcast
 * @create 2023/9/21 15:19
 **/
@Service
public class OrdersStatisticsServiceImpl implements OrdersStatisticsService {
    @Resource
    private IStatDayService statDayService;
    @Resource
    private IStatHourService statHourService;

    /**
     * 最小小时值
     */
    private static final String MIN_HOUR = "00";
    /**
     * 最大小时值
     */
    private static final String MAX_HOUR = "23";

    /**
     * 运营端首页数据
     *
     * @param minTime 开始时间
     * @param maxTime 结束时间
     * @return 首页数据
     */
    @Override
    @Cacheable(value = "JZ_CACHE", cacheManager = "cacheManager30Minutes")
    public OperationHomePageResDTO homePage(LocalDateTime minTime, LocalDateTime maxTime) {
        //校验查询时间
        if (LocalDateTimeUtil.between(minTime, maxTime, ChronoUnit.DAYS) > 365) {
            throw new ForbiddenOperationException("查询时间区间不能超过一年");
        }

        //如果查询日期是同一天，则按小时查询折线图数据
        if (LocalDateTimeUtil.beginOfDay(maxTime).equals(minTime)) {
            return getHourOrdersStatistics(minTime);
        } else {
            //如果查询日期不是同一天，则按日查询折线图数据
            return getDayOrdersStatistics(minTime, maxTime);
        }
    }

    /**
     * 按日统计数据
     *
     * @param minTime 最小时间
     * @param maxTime 最大时间
     * @return 统计数据
     */
    private OperationHomePageResDTO getDayOrdersStatistics(LocalDateTime minTime, LocalDateTime maxTime) {
        //定义要返回的对象
        OperationHomePageResDTO operationHomePageResDTO = OperationHomePageResDTO.defaultInstance();

        //日期格式化，格式：yyyyMMdd
        String minTimeDayStr = LocalDateTimeUtil.format(minTime, DatePattern.PURE_DATE_PATTERN);
        String maxTimeDayStr = LocalDateTimeUtil.format(maxTime, DatePattern.PURE_DATE_PATTERN);

        //根据日期区间聚合统计数据
        StatDay statDay = statDayService.aggregationByIdRange(Long.valueOf(minTimeDayStr), Long.valueOf(maxTimeDayStr));

        //将statDay拷贝到operationHomePageResDTO
        operationHomePageResDTO = BeanUtils.copyIgnoreNull(BeanUtil.toBean(statDay, OperationHomePageResDTO.class), operationHomePageResDTO, OperationHomePageResDTO.class);

        //根据日期区间查询按日统计数据
        List<StatDay> statDayList = statDayService.queryListByIdRange(Long.valueOf(minTimeDayStr), Long.valueOf(maxTimeDayStr));
        //将statDayList转为map<趋势图横坐标，订单总数>
        Map<String, Integer> ordersCountMap = statDayList.stream().collect(Collectors.toMap(s -> dateFormatter(s.getId()), StatDay::getTotalOrderNum));
        //趋势图上全部点
        List<OperationHomePageResDTO.OrdersCount> ordersCountsDef = OperationHomePageResDTO.defaultDayOrdersTrend(minTime, maxTime);
        //遍历ordersCountsDef，将统计出来的ordersCountMap覆盖ordersCountsDef中的数据
        ordersCountsDef.stream().forEach(v->{
            if (ObjectUtil.isNotEmpty(ordersCountMap.get(v.getDateTime()))) {
                v.setCount(ordersCountMap.get(v.getDateTime()));
            }
        });
        //将ordersCountsDef放入operationHomePageResDTO
        operationHomePageResDTO.setOrdersTrend(ordersCountsDef);

        return operationHomePageResDTO;
    }

    /**
     * 日期格式化，原格式为：yyyyMMdd,保留月和日，如：9.1
     *
     * @return 日期
     */
    private String dateFormatter(Long dateTime) {
        String dateTimeStr = String.valueOf(dateTime);
        String month = dateTimeStr.substring(4, 6);
        month = month.startsWith("0") ? month.substring(1, 2) : month;

        String day = dateTimeStr.substring(6, 8);
        day = day.startsWith("0") ? day.substring(1, 2) : day;
        return month + "." + day;
    }


    /**
     * 按小时统计数据
     *
     * @param minTime 开始时间
     * @return 统计数据
     */
    private OperationHomePageResDTO getHourOrdersStatistics(LocalDateTime minTime) {
        //定义要返回的对象
        OperationHomePageResDTO operationHomePageResDTO = OperationHomePageResDTO.defaultInstance();

        //获取当前日期，格式：yyyyMMdd
        String minTimeDayStr = LocalDateTimeUtil.format(minTime, DatePattern.PURE_DATE_PATTERN);

        //查询该日期的统计数据
        StatDay statDay = statDayService.getById(Long.valueOf(minTimeDayStr));

        //趋势图上全部点
        List<OperationHomePageResDTO.OrdersCount> ordersCountsDef = OperationHomePageResDTO.defaultHourOrdersTrend();
        if (null == statDay) {
            operationHomePageResDTO.setOrdersTrend(ordersCountsDef);
            return operationHomePageResDTO;
        }

        //如果统计数据不为空，拷贝数据
        operationHomePageResDTO = BeanUtil.toBean(statDay, OperationHomePageResDTO.class);

        //根据时间区间查询小时统计数据，并转换为map结构，key为小时，value为订单数量
        List<StatHour> statHourList = statHourService.queryListByIdRange(Long.valueOf(minTimeDayStr + MIN_HOUR), Long.valueOf(minTimeDayStr + MAX_HOUR));
        //将statHourList转map
        Map<String, Integer> ordersCountMap = statHourList.stream().collect(Collectors.toMap(s -> String.format("%02d",s.getId() % 100), StatHour::getTotalOrderNum));
        //遍历ordersCountsDef，将统计出来的ordersCountMap覆盖ordersCountsDef中的数据
        ordersCountsDef.stream().forEach(v->{
            if (ObjectUtil.isNotEmpty(ordersCountMap.get(v.getDateTime()))) {
                v.setCount(ordersCountMap.get(v.getDateTime()));
            }
        });

        //组装订单数趋势，返回结果
        operationHomePageResDTO.setOrdersTrend(ordersCountsDef);
        return operationHomePageResDTO;
    }


    /**
     * 导出统计数据
     *
     * @param minTime 开始时间
     * @param maxTime 结束时间
     */
    @Override
    public void downloadStatistics(LocalDateTime minTime, LocalDateTime maxTime) throws IOException {
        //校验查询时间
        if (LocalDateTimeUtil.between(minTime, maxTime, ChronoUnit.DAYS) > 365) {
            throw new ForbiddenOperationException("查询时间区间不能超过一年");
        }

        HttpServletResponse response = ResponseUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        //设置返回body无需包装标识
        response.setHeader(BODY_PROCESSED, "1");
        try {
            //如果查询日期是同一天，则按小时导出数据
            if (LocalDateTimeUtil.beginOfDay(maxTime).equals(minTime)) {
                downloadHourStatisticsData(response, minTime);
            } else {
                //如果查询日期不是同一天，则按日导出数据
                downloadDayStatisticsData(response, minTime, maxTime);
            }

        } catch (Exception e) {
            // 重置response,默认失败了会返回一个有部分数据的Excel
            response.reset();
            response.setContentType("application/json");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    private void downloadDayStatisticsData(HttpServletResponse response, LocalDateTime minTime, LocalDateTime maxTime) throws IOException {
        //模板文件路径
        String templateFileName = "static/day_statistics_template.xlsx";

        //转换时间格式，拼接下载文件名称
        String fileNameMinTimeStr = LocalDateTimeUtil.format(minTime, DatePattern.NORM_DATE_PATTERN);
        String fileNameMaxTimeStr = LocalDateTimeUtil.format(maxTime, DatePattern.NORM_DATE_PATTERN);
        String fileName = fileNameMinTimeStr + "~" + fileNameMaxTimeStr + " 全国经营分析统计.xlsx";
        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        String currentTime = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy/MM/dd HH:mm:ss");

        //根据id区间查询按天统计数据，并转为map，key为日期，value为日期对应统计数据
        String minTimeDayStr = LocalDateTimeUtil.format(minTime, DatePattern.PURE_DATE_PATTERN);
        String maxTimeDayStr = LocalDateTimeUtil.format(maxTime, DatePattern.PURE_DATE_PATTERN);
        //查询按天统计表
        List<StatDay> statDayList = statDayService.queryListByIdRange(Long.valueOf(minTimeDayStr), Long.valueOf(maxTimeDayStr));
        //转成List<StatisticsData>
        List<StatisticsData> statisticsDataList = BeanUtils.copyToList(statDayList, StatisticsData.class);

        //按月份切分统计数据， 有几个月list中就有几条记录
        List<ExcelMonthData> excelMonthDataList = cutDataListByMonth(statisticsDataList);

        // 生成CellWriteHandler对象，在向单元格写数据时会调用它的afterCellDispose方法
        //  getSpecialHandleDataInfo()方法找到需要格式化处理的行索引，返回CellWriteHandler对象
        EasyExcelUtil easyExcelUtil = getSpecialHandleDataInfo(excelMonthDataList);

        try (ExcelWriter excelWriter = EasyExcel
                //注意，服务器上以jar包运行，只能使用下面第2种方式，第1种方式只能在本地运行成功
//                .write(fileName, StatisticsData.class)
                .write(response.getOutputStream(), StatisticsData.class)
                //注意，服务器上以jar包运行，只能使用下面第3种方式，前2种方式只能在本地运行成功
//                .withTemplate(templateFileName)
//                .withTemplate(FileUtil.getInputStream(templateFileName))
                .withTemplate(FileUtil.class.getClassLoader().getResourceAsStream(templateFileName))
                .autoCloseStream(Boolean.FALSE)
                .registerWriteHandler(easyExcelUtil).build()) {
            // 按天统计，选择第1个sheet，把sheet设置为不需要头
            WriteSheet writeSheet = EasyExcel.writerSheet(0).needHead(Boolean.FALSE).build();

            //构建填充数据,map的key对应模板文件中的{}中的名称
            Map<String, Object> map = MapUtils.newHashMap();
            map.put("startTime", fileNameMinTimeStr);
            map.put("endTime", fileNameMaxTimeStr);
            map.put("currentTime", currentTime);


            //写入填充数据
            excelWriter.fill(map, writeSheet);
            //向单元格式依次写入数据
            for (ExcelMonthData excelMonthData : excelMonthDataList) {
                MonthElement monthElement = new MonthElement(excelMonthData.getMonth());
                excelWriter.write(List.of(monthElement), writeSheet);//月份
                excelWriter.write(excelMonthData.getStatisticsDataList(), writeSheet);//每天的数据
                excelWriter.write(List.of(excelMonthData.getMonthAggregation()), writeSheet);//该天的汇总数据
            }

            excelWriter.finish();
        }
    }
//    private void downloadDayStatisticsData(HttpServletResponse response, LocalDateTime minTime, LocalDateTime maxTime) throws IOException {
//        //1.模板文件路径
//        String templateFileName = "static/day_statistics_template.xlsx";
//
//        //2.转换时间格式，拼接下载文件名称
//        String fileNameMinTimeStr = LocalDateTimeUtil.format(minTime, DatePattern.NORM_DATE_PATTERN);
//        String fileNameMaxTimeStr = LocalDateTimeUtil.format(maxTime, DatePattern.NORM_DATE_PATTERN);
//        String fileName = fileNameMinTimeStr + "~" + fileNameMaxTimeStr + " 全国经营分析统计.xlsx";
//        // 这里URLEncoder.encode可以防止中文乱码
//        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
//        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
//        String currentTime = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy/MM/dd HH:mm:ss");
//
//        //3.根据id区间查询按天统计数据，并转为map，key为日期，value为日期对应统计数据
//        String minTimeDayStr = LocalDateTimeUtil.format(minTime, DatePattern.PURE_DATE_PATTERN);
//        String maxTimeDayStr = LocalDateTimeUtil.format(maxTime, DatePattern.PURE_DATE_PATTERN);
//        List<StatDay> statDayList = statDayService.queryListByIdRange(Long.valueOf(minTimeDayStr), Long.valueOf(maxTimeDayStr));
//        Map<String, StatDay> ordersCountMap = statDayList.stream().collect(Collectors.toMap(s -> String.valueOf(s.getId()), s -> s));
//
//        //4.将存在统计数据的对应日期数据替换掉默认值
//        List<StatisticsData> statisticsDataList = StatisticsData.defaultDayStatisticsData(minTime, maxTime);
//        for (StatisticsData statisticsData : statisticsDataList) {
//            if (ObjectUtil.isNotEmpty(ordersCountMap.get(statisticsData.getStatTime()))) {
//                BeanUtil.copyProperties(ordersCountMap.get(statisticsData.getStatTime()), statisticsData, "statTime");
//            }
//        }
//
//        //5.按月份切分统计数据
//        List<ExcelMonthData> excelMonthDataList = cutDataListByMonth(statisticsDataList);
//
//        // 调用合并单元格工具类，此工具类是根据工程名称相同则合并后面数据
//        EasyExcelUtil easyExcelUtil = getSpecialHandleDataInfo(excelMonthDataList);
//
//        try (ExcelWriter excelWriter = EasyExcel
//                //注意，服务器上以jar包运行，只能使用下面第2种方式，第1种方式只能在本地运行成功
////                .write(fileName, StatisticsData.class)
//                .write(response.getOutputStream(), StatisticsData.class)
//                //注意，服务器上以jar包运行，只能使用下面第3种方式，前2种方式只能在本地运行成功
////                .withTemplate(templateFileName)
////                .withTemplate(FileUtil.getInputStream(templateFileName))
//                .withTemplate(FileUtil.class.getClassLoader().getResourceAsStream(templateFileName))
//                .autoCloseStream(Boolean.FALSE)
//                .registerWriteHandler(easyExcelUtil).build()) {
//            // 6.按天统计，选择第1个sheet，把sheet设置为不需要头
//            WriteSheet writeSheet = EasyExcel.writerSheet(0).needHead(Boolean.FALSE).build();
//
//            //7.构建填充数据
//            Map<String, Object> map = MapUtils.newHashMap();
//            map.put("startTime", fileNameMinTimeStr);
//            map.put("endTime", fileNameMaxTimeStr);
//            map.put("currentTime", currentTime);
//
//
//            //8.依次写入填充数据、月份值、按天统计数据、聚合统计数据
//            excelWriter.fill(map, writeSheet);
//            for (ExcelMonthData excelMonthData : excelMonthDataList) {
//                MonthElement monthElement = new MonthElement(excelMonthData.getMonth());
//                excelWriter.write(List.of(monthElement), writeSheet);
//                excelWriter.write(excelMonthData.getStatisticsDataList(), writeSheet);
//                excelWriter.write(List.of(excelMonthData.getMonthAggregation()), writeSheet);
//            }
//
//            excelWriter.finish();
//        }
//    }

    /**
     * 获取需要特殊处理单元格样式的数据信息
     *
     * @param excelMonthDataList 按月份切分的统计数据
     * @return EasyExcel工具类
     */
    private EasyExcelUtil getSpecialHandleDataInfo(List<ExcelMonthData> excelMonthDataList) {
        List<Integer> monthRowIndexList = new ArrayList<>();
        List<Integer> aggregationRowIndexList = new ArrayList<>();

        //聚合统计数据所在excel行的索引
        int rowIndex = -1;
        for (ExcelMonthData excelMonthData : excelMonthDataList) {

            //该月拥有的统计数据条数
            int dayNum = excelMonthData.getStatisticsDataList().size();
            monthRowIndexList.add(++rowIndex);
            rowIndex = rowIndex+dayNum+1;
            aggregationRowIndexList.add(rowIndex);

//            if (aggregationRowIndex == 0) {
//                aggregationRowIndex += dayNum + 1;
//            } else {
//                aggregationRowIndex += dayNum + 2;
//            }

//            monthRowIndexList.add(aggregationRowIndex - dayNum - 1);
//            aggregationRowIndexList.add(aggregationRowIndex);
        }

        //excel表头有4行数据，所以索引从4开始计数
        monthRowIndexList = monthRowIndexList.stream().map(m -> m += 4).collect(Collectors.toList());
        aggregationRowIndexList = aggregationRowIndexList.stream().map(m -> m += 4).collect(Collectors.toList());

        return new EasyExcelUtil(monthRowIndexList, aggregationRowIndexList);
    }


    /**
     * 按月切分统计数据
     *
     * @param statisticsDataList 统计数据
     * @return 切分数据
     */
    private List<ExcelMonthData> cutDataListByMonth(List<StatisticsData> statisticsDataList) {
        //转换时间格式：由yyyyMMdd转换为LocalDateTime
        LocalDateTime minTime = LocalDateTimeUtil.parse(statisticsDataList.get(0).getStatTime(), DatePattern.PURE_DATE_PATTERN);
        LocalDateTime maxTime = LocalDateTimeUtil.parse(statisticsDataList.get(statisticsDataList.size() - 1).getStatTime(), DatePattern.PURE_DATE_PATTERN);

        //切分出月份（格式：yyyyMM），分组统计
        Map<String, List<StatisticsData>> collect = statisticsDataList.stream().collect(Collectors.groupingBy(s -> String.valueOf(s.getStatTime()).substring(0, 6)));

        List<ExcelMonthData> excelMonthDataList = new ArrayList<>();
        while (true) {
            //minTime转换为yyyyMMdd格式，取出对应分组数据
            List<StatisticsData> monthStatisticsDataList = collect.get(LocalDateTimeUtil.format(minTime, DatePattern.SIMPLE_MONTH_PATTERN));
            monthStatisticsDataList.forEach(s -> s.setStatTime(excelDateFormatter(s.getStatTime())));

            //按月聚合统计数据，求和或求平均数
            AggregationStatisticsData aggregationStatisticsData = getAggregationStatisticsData(monthStatisticsDataList, "月统计");

            //封装excel月度数据
            ExcelMonthData excelMonthData = new ExcelMonthData();
            int month = minTime.getMonthValue();
            excelMonthData.setMonth(month + "月");
            excelMonthData.setStatisticsDataList(monthStatisticsDataList);
            excelMonthData.setMonthAggregation(aggregationStatisticsData);
            excelMonthDataList.add(excelMonthData);

            //minTime自增，直到minTime>maxTime循环结束
            minTime = minTime.plusMonths(1);
            if (minTime.isAfter(maxTime)) {
                break;
            }
        }
        return excelMonthDataList;
    }

    /**
     * excel表格日期格式化，原格式为：yyyyMMdd,保留月和日，如：9月1日
     *
     * @return 日期
     */
    private String excelDateFormatter(String dateTime) {
        String dateTimeStr = String.valueOf(dateTime);
        String month = dateTimeStr.substring(4, 6);
        month = month.startsWith("0") ? month.substring(1, 2) : month;

        String day = dateTimeStr.substring(6, 8);
        day = day.startsWith("0") ? day.substring(1, 2) : day;
        return month + "月" + day + "日";
    }


    /**
     * 下载按小时统计数据
     *
     * @param response 响应体
     * @param minTime  开始时间
     */
    private void downloadHourStatisticsData(HttpServletResponse response, LocalDateTime minTime) throws IOException {
        //1.模板文件路径
        String templateFileName = "static/hour_statistics_template.xlsx";

        //2.转换时间格式，拼接下载文件名称
        String dateTimeStr = LocalDateTimeUtil.format(minTime, DatePattern.NORM_DATE_PATTERN);
        String fileName = dateTimeStr + " 全国经营分析统计.xlsx";
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        String currentTime = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy/MM/dd HH:mm:ss");

        //3.根据时间区间查询小时统计数据，并转换为map结构，key为小时，value为按小时统计数据
        String minTimeDayStr = LocalDateTimeUtil.format(minTime, DatePattern.PURE_DATE_PATTERN);
        List<StatHour> statHourList = statHourService.queryListByIdRange(Long.valueOf(minTimeDayStr + MIN_HOUR), Long.valueOf(minTimeDayStr + MAX_HOUR));
        //转成List<StatisticsData>
        List<StatisticsData> statisticsDataList = BeanUtils.copyToList(statHourList, StatisticsData.class,(o, t)->{
            t.setStatTime(String.valueOf(t.getStatTime()).substring(8, 10) + "时");
        });


        //5.按天统计数据，求和或求平均值
        AggregationStatisticsData aggregationStatisticsData = getAggregationStatisticsData(statisticsDataList, "日统计");

        try (ExcelWriter excelWriter = EasyExcel
                .write(response.getOutputStream(), StatisticsData.class)
                .withTemplate(FileUtil.class.getClassLoader().getResourceAsStream(templateFileName))
                .autoCloseStream(Boolean.FALSE).build()) {
            // 6.按小时统计，选择第二个sheet，把sheet设置为不需要头
            WriteSheet writeSheet = EasyExcel.writerSheet(0).needHead(Boolean.FALSE).build();

            //7.构建填充数据
            Map<String, Object> map = MapUtils.newHashMap();
            map.put("startTime", dateTimeStr);
            map.put("currentTime", currentTime);


            //8.依次写入填充数据、按小时统计数据、聚合统计数据
            excelWriter.fill(map, writeSheet);
            excelWriter.write(statisticsDataList, writeSheet);
            excelWriter.write(List.of(aggregationStatisticsData), writeSheet);
            excelWriter.finish();
        }
    }

    /**
     * 数据聚合统计
     *
     * @param statisticsDataList excel中按小时或按天统计数据
     * @param statisticsName     聚合统计名称
     * @return 聚合统计数据
     */
    private AggregationStatisticsData getAggregationStatisticsData(List<StatisticsData> statisticsDataList, String statisticsName) {
        Integer totalOrderNum = statisticsDataList.stream().map(StatisticsData::getTotalOrderNum).reduce(Integer::sum).orElse(0);
        Integer effectiveOrderNum = statisticsDataList.stream().map(StatisticsData::getEffectiveOrderNum).reduce(Integer::sum).orElse(0);
        Integer cancelOrderNum = statisticsDataList.stream().map(StatisticsData::getCancelOrderNum).reduce(Integer::sum).orElse(0);
        Integer closeOrderNum = statisticsDataList.stream().map(StatisticsData::getCloseOrderNum).reduce(Integer::sum).orElse(0);
        Double effectiveOrderTotalAmountDoubleValue = statisticsDataList.stream().map(s -> s.getEffectiveOrderTotalAmount().doubleValue()).reduce(Double::sum).orElse(0.0);
        BigDecimal effectiveOrderTotalAmount = NumberUtil.toBigDecimal(effectiveOrderTotalAmountDoubleValue);
        BigDecimal realPayAveragePrice = effectiveOrderNum == 0 ? BigDecimal.ZERO : NumberUtil.div(effectiveOrderTotalAmount, effectiveOrderNum, 2);

        return AggregationStatisticsData.builder()
                .statTime(statisticsName)
                .totalOrderNum(totalOrderNum)
                .effectiveOrderNum(effectiveOrderNum)
                .cancelOrderNum(cancelOrderNum)
                .closeOrderNum(closeOrderNum)
                .effectiveOrderTotalAmount(effectiveOrderTotalAmount)
                .realPayAveragePrice(realPayAveragePrice)
                .build();
    }
}
