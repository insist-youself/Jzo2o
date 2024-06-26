package com.jzo2o.api.market.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("优惠券使用退回请求模型")
@Data
public class CouponUseBackReqDTO {
    @ApiModelProperty("优惠券id")
    private Long id;
    @ApiModelProperty("订单id")
    private Long ordersId;
    @ApiModelProperty("用户id")
    private Long userId;
}
