package com.jzo2o.orders.dispatch.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@ApiModel("派单列表响应参数")
@NoArgsConstructor
@AllArgsConstructor
public class DispatchQueryListResDTO {
    @ApiModelProperty("派单列表")
    private List<OrdersDispatchResDTO> list;
}
