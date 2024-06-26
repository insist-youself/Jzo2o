package com.jzo2o.orders.manager.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author itcast
 */
@Data
public class OrdersServeAllocationReqDTO {

    @ApiModelProperty("服务单id")
    @NotNull(message = "人员分配失败")
    private Long id;
    @ApiModelProperty("服务人员id")
    @NotNull(message = "人员分配失败")
    private Long institutionStaffId;
}
