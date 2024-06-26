package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务人员申请认证信息响应值
 *
 * @author itcast
 * @create 2023/9/6 11:51
 **/
@Data
@ApiModel("服务人员申请认证信息驳回原因响应值")
public class WorkerCertificationAuditRejectReasonResDTO {
    /**
     * 驳回原因
     */
    @ApiModelProperty("驳回原因")
    private String rejectReason;

}
