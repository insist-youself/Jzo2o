package com.jzo2o.market.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("活动分页查询模型")
public class ActivityQueryForPageReqDTO extends PageQueryDTO {
    @ApiModelProperty("活动id")
    private Long id;
    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty("类型，，1：满减，2：折扣")
    private Integer type;
    @ApiModelProperty("优惠券配置状态，1：待生效，2：进行中，3：已失效")
    private Integer status;
}
