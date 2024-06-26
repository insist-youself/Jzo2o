package com.jzo2o.orders.seize.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("抢单模型")
public class OrdersSeizeReqDTO {
    /**
     * 抢单id
     */
    @ApiModelProperty("抢单id")
    private Long id;
}
