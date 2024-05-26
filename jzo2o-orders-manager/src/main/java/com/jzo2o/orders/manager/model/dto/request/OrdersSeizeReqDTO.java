package com.jzo2o.orders.manager.model.dto.request;

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
