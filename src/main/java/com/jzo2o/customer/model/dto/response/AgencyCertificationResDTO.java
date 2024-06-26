package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 机构认证信息响应值
 *
 * @author itcast
 * @create 2023/9/6 11:51
 **/
@Data
@ApiModel("机构认证信息响应值")
public class AgencyCertificationResDTO {
    /**
     * 主键
     */
    @ApiModelProperty("机构id")
    private Long id;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
    private String idNumber;

    /**
     * 法人姓名
     */
    @ApiModelProperty("法人姓名")
    private String legalPersonName;

    /**
     * 法人身份证号
     */
    @ApiModelProperty("法人身份证号")
    private String legalPersonIdCardNo;

    /**
     * 营业执照
     */
    @ApiModelProperty("营业执照")
    private String businessLicense;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
