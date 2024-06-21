package com.jzo2o.orders.seize.model.domain;

import com.jzo2o.common.model.Location;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 用于向ES存储抢单池信息
 */
@Data
public class OrdersSeizeInfo {
    /**
     * 抢单id，和订单id保持一致
     */
    private Long id;

    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 分类id
     */
    private Long serveTypeId;
    /**
     * 服务项id
     */
    private Long serveItemId;

    /**
     * 服务类型名称
     */
    private String serveTypeName;

    /**
     * 服务项名称
     */
    private String serveItemName;

    /**
     * 服务地址
     */
    private String serveAddress;

    /**
     * 服务时间，格式yyyyMMddHH
     */
    private Integer serveTime;

    /**
     * 地理坐标，经纬度
     */
    private Location location;

    /**
     * 服务开始时间
     */
    private LocalDateTime serveStartTime;

    /**
     * 服务数量
     */
    private Integer purNum;

    /**
     * 订单总金额
     */
    private Double totalAmount;

    /**
     * 关键字检索字段
     */
    private String keyWords;

    /**
     * 订单金额
     */
    private BigDecimal ordersAmount;

    /**
     * 服务项图片
     */
    private String serveItemImg;

}
