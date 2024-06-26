package com.jzo2o.orders.manager.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel("下单请求信息")
@Data
public class PlaceOrderReqDTO {
    @ApiModelProperty(value = "服务id",required = true)
    @NotNull(message = "您还未选择服务")
    private Long serveId;

    @ApiModelProperty(value = "预约地址id",required = true)
    @NotNull(message = "您还未选择服务地址")
    private Long addressBookId;

    @ApiModelProperty(value = "购买数量",required = false)
    private Integer purNum;

    @ApiModelProperty(value = "预约时间",required = true)
    private LocalDateTime serveStartTime;

    @ApiModelProperty(value = "优惠券id", required = false)
    private Long couponId;
}
