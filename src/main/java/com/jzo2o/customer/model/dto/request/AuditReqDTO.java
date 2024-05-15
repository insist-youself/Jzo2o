package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户提交举报信息
 *
 * @author itcast
 * @create 2023/6/2 15:47
 **/
@Data
@ApiModel("用户提交举报信息")
public class AuditReqDTO {
    /**
     * 对象类型id
     */
    @ApiModelProperty(value = "审核id", required = true)
    private String targetTypeId;

    /**
     * 业务类型。1：评价，3回复
     */
    @ApiModelProperty(value = "业务类型。1：评价，3回复", required = true)
    private Integer bizType = 1;

    /**
     * 业务id，评价/回复id
     */
    @ApiModelProperty(value = "业务id，评价/回复id", required = true)
    private String bizId;

    /**
     * 审核原因
     */
    @ApiModelProperty(value = "审核原因", required = true)
    private Integer reason;
}