package com.jzo2o.orders.manager.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author itcast
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("服务单状态的数量")
public class OrdersServeStatusNumResDTO {
    @ApiModelProperty(required = true, value = "未分配")
    private Long noAllocation;
    @ApiModelProperty(required = true, value = "未开始服务")
    private Long noServed;
    @ApiModelProperty(required = true, value = "服务中，待服务完成")
    private Long serving;

    public static OrdersServeStatusNumResDTO empty() {
        return new OrdersServeStatusNumResDTO(0L,0L,0L);
    }
}
