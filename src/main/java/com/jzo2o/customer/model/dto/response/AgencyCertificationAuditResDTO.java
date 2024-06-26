package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 机构申请认证信息响应值
 *
 * @author itcast
 * @create 2023/9/6 11:51
 **/
@Data
@ApiModel("机构申请认证信息响应值")
public class AgencyCertificationAuditResDTO {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 服务人员id
     */
    @ApiModelProperty("服务人员id")
    private Long serveProviderId;

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
     * 审核状态，0：未审核，1：已审核
     */
    @ApiModelProperty("审核状态，0：未审核，1：已审核")
    private Integer auditStatus;

    /**
     * 审核人id
     */
    @ApiModelProperty("审核人id")
    private Long auditorId;

    /**
     * 审核人姓名
     */
    @ApiModelProperty("审核人姓名")
    private String auditorName;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;

    /**
     * 认证状态，1：认证中，2：认证成功，3认证失败
     */
    @ApiModelProperty("认证状态，1：认证中，2：认证成功，3认证失败")
    private Integer certificationStatus;

    /**
     * 驳回原因
     */
    @ApiModelProperty("驳回原因")
    private String rejectReason;

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
