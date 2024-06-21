package com.jzo2o.orders.dispatch.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author itcast
 */
@ApiModel("接单模型")
@Data
public class DispatchRejectReqDTO {
    @ApiModelProperty(value = "派单id",required = true)
    @NotNull(message = "接单失败")
    private Long id;
    @ApiModelProperty(value = "拒单原因",required = true)
    @NotNull(message = "拒单失败")
    private String rejectReason;
}
