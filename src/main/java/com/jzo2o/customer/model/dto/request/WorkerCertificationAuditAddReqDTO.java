package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务人员申请资质认证
 *
 * @author itcast
 * @create 2023/9/6 10:24
 **/
@Data
@ApiModel("服务人员认证申请请求体")
public class WorkerCertificationAuditAddReqDTO {
    @ApiModelProperty(value = "服务人员id", required = false)
    private Long serveProviderId;

    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @ApiModelProperty(value = "身份证号", required = true)
    private String idCardNo;

    @ApiModelProperty(value = "身份证正面", required = true)
    private String frontImg;

    @ApiModelProperty(value = "身份证反面", required = true)
    private String backImg;

    @ApiModelProperty(value = "证明资料", required = true)
    private String certificationMaterial;
}
