package com.jzo2o.api.market.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("优惠券使用模型")
public class CouponUseReqDTO {
    @ApiModelProperty("优惠券id")
    private Long id;
    @ApiModelProperty("订单id")
    private Long ordersId;
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;
}
