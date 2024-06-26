package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 驳回原因
 *
 * @author itcast
 * @create 2023/9/7 09:29
 **/
@Data
@ApiModel("驳回原因响应数据")
@NoArgsConstructor
@AllArgsConstructor
public class RejectReasonResDTO {
    @ApiModelProperty("驳回原因")
    private String rejectReason;
}
