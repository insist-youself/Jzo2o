package com.jzo2o.market.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Null;

@Data
@ApiModel("运营端优惠券查询模型")
public class CouponOperationPageQueryReqDTO extends PageQueryDTO {
    @ApiModelProperty(value = "活动id",required = true)
    @Null(message = "请先选择活动")
    private Long activityId;
}
