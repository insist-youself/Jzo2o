package com.jzo2o.orders.dispatch.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author itcast
 */
@Data
@ApiModel("派单列表")
public class OrdersDispatchResDTO {
    @ApiModelProperty(value = "派单id",required = true)
    private Long id;
    @ApiModelProperty(value = "分类名称",required = true)
    private String serveTypeName;
    @ApiModelProperty(value = "服务项名称",required = true)
    private String serveItemName;
    @ApiModelProperty(value = "服务费",required = true)
    private BigDecimal serveFee;
    @ApiModelProperty(value = "预约时间",required = true)
    private LocalDateTime serveStartTime;
    @ApiModelProperty(value = "服务地址",required = true)
    private String serveAddress;
    @ApiModelProperty(value = "服务项图片",required = true)
    private String serveItemImg;
    @ApiModelProperty(value = "订单金额",required = true)
    private BigDecimal ordersAmount;

}
