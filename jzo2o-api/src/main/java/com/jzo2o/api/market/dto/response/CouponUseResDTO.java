package com.jzo2o.api.market.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("优惠券使用返回信息模型")
public class CouponUseResDTO {
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;
}
