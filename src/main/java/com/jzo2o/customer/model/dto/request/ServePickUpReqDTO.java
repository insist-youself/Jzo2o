package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("接单设置")
public class ServePickUpReqDTO {

    /**
     * 是否开启接单设置
     */
    @ApiModelProperty("是否开启接单设置设置,0:关闭接单，1：开启接单")
    private Integer canPickUp;
}
