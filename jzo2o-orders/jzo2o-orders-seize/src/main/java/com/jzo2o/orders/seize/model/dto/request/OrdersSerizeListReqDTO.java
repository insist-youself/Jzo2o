package com.jzo2o.orders.seize.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@ApiModel("抢单列表请求参数")
public class OrdersSerizeListReqDTO {

    /**
     * 关键字，查询服务项名称，服务名称，服务地址
     */
    @ApiModelProperty("关键字，查询服务项名称，服务名称，服务地址")
    private String keyWord;
    /**
     * 服务类型id
     */
    @ApiModelProperty(value = "服务类型id")
    private Long serveTypeId;
    /**
     * 距离
     */
    @ApiModelProperty(value = "服务距离，用于筛选条件")
    private Double serveDistance;

    /**
     * 上一页最后一条数据的距离字段、，用于滚动分页
     */
    @ApiModelProperty(value = "上一页最后一条数据的距离字段、，用于滚动分页")
    private Double lastRealDistance;

    /**
     * 预约时间查询下限
     */
    @ApiModelProperty("预约时间查询下限")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime minServeStartTime;

    /**
     * 预约时间查询上限
     */
    @ApiModelProperty("预约时间查询上限")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime maxServeStartTime;

}
