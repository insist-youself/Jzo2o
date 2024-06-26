package com.jzo2o.orders.manager.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@ApiModel("服务单列表，无分页")
@NoArgsConstructor
@AllArgsConstructor
public class OrdersServeListResDTO {

    @ApiModelProperty("服务单列表")
    private List<OrdersServeResDTO> ordersServes;
}
