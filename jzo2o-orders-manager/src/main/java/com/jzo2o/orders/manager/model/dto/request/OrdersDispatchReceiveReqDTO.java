package com.jzo2o.orders.manager.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author itcast
 */
@ApiModel("接单模型")
@Data
public class OrdersDispatchReceiveReqDTO {
    @ApiModelProperty(value = "派单id",required = true)
    @NotNull(message = "接单失败")
    private Long id;
}
