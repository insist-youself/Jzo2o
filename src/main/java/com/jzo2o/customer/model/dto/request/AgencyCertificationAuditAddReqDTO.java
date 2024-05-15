package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 机构认证申请
 *
 * @author itcast
 * @create 2023/9/6 09:30
 **/
@Data
@ApiModel("机构认证申请")
public class AgencyCertificationAuditAddReqDTO {
    @ApiModelProperty(value = "机构id", required = false)
    private Long serveProviderId;

    @ApiModelProperty(value = "企业名称", required = true)
    private String name;

    @ApiModelProperty(value = "统一社会信用代码", required = true)
    private String idNumber;

    @ApiModelProperty(value = "法人姓名", required = true)
    private String legalPersonName;

    @ApiModelProperty(value = "法人身份证号", required = true)
    private String legalPersonIdCardNo;

    @ApiModelProperty(value = "营业执照", required = true)
    private String businessLicense;
}
