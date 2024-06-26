package com.jzo2o.orders.history.controller.operation;

import com.jzo2o.orders.history.model.dto.response.OperationHomePageResDTO;
import com.jzo2o.orders.history.service.OrdersStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 订单统计
 *
 * @author itcast
 * @create 2023/9/21 15:15
 **/
@Api(tags = "运营端 - 订单统计相关接口")
@RestController("operationOrdersStatisticsController")
@RequestMapping("/operation/orders-statistics")
public class OrdersStatisticsController {
    @Resource
    private OrdersStatisticsService ordersStatisticsService;

    @GetMapping("/homePage")
    @ApiOperation("运营端首页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "minTime", value = "开始时间", required = true, dataTypeClass = LocalDateTime.class),
            @ApiImplicitParam(name = "maxTime", value = "结束时间", required = true, dataTypeClass = LocalDateTime.class)
    })
    public OperationHomePageResDTO homePage(@RequestParam("minTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime minTime,
                                            @RequestParam("maxTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime maxTime) {
        return ordersStatisticsService.homePage(minTime, maxTime);
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @GetMapping("downloadStatistics")
    @ApiOperation("导出统计数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "minTime", value = "开始时间", required = true, dataTypeClass = LocalDateTime.class),
            @ApiImplicitParam(name = "maxTime", value = "结束时间", required = true, dataTypeClass = LocalDateTime.class)
    })
    public void downloadStatistics(@RequestParam("minTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime minTime,
                                   @RequestParam("maxTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime maxTime) throws IOException {
        ordersStatisticsService.downloadStatistics(minTime, maxTime);
    }
}
