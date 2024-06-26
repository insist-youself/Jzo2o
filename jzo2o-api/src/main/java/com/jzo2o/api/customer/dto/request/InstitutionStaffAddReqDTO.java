package com.jzo2o.api.customer.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("机构服务人员添加接口")
public class InstitutionStaffAddReqDTO {

    @ApiModelProperty("机构服务人员id")
    private Long id;

    @ApiModelProperty("机构id")
    private Long institutionId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话", required = true)
    private String phone;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCardNo;

    /**
     * 证明资料列表
     */
    @ApiModelProperty("证明资料列表")
    private List<String> certificationImgs;
}
