package com.jzo2o.market.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Null;

@Data
@ApiModel
public class SeizeCouponReqDTO {
    @ApiModelProperty("活动id")
    @Null(message = "请求失败")
    private Long id;
}
