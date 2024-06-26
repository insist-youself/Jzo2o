package com.jzo2o.orders.manager.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author itcast
 */
@Data
@ApiModel("服务取消模型")
public class OrderServeCancelReqDTO {
    @ApiModelProperty(value = "服务单id",required = true)
    @NotNull(message = "操作失败")
    private Long id;

    @ApiModelProperty(value = "取消原因",required = true)
    @NotNull(message = "操作失败")
    private String cancelReason;

}
