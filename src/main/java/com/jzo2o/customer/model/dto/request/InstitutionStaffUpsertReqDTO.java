package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 插入更新机构下属服务人员
 *
 * @author itcast
 * @create 2023/7/18 20:20
 **/
@Data
@ApiModel("插入更新机构下属服务人员")
public class InstitutionStaffUpsertReqDTO {

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
