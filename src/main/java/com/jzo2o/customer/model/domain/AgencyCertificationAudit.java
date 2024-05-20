package com.jzo2o.customer.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 机构认证审核信息表
 * </p>
 *
 * @author linger
 * @since 2024-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("agency_certification_audit")
public class AgencyCertificationAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 服务人员id
     */
    private Long serveProviderId;

    /**
     * 审核人id
     */
    private Long auditorId;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 统一社会信用代码
     */
    private String idNumber;

    /**
     * 法人姓名
     */
    private String legalPersonName;

    /**
     * 法人身份证号
     */
    private String legalPersonIdCardNo;

    /**
     * 营业执照
     */
    private String businessLicense;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 认证状态，0：初始态，1：认证中，2：认证成功，3认证失败
     */
    private Integer certificationStatus;

    /**
     * 审核人姓名
     */
    private String auditorName;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核状态，0：未审核，1：已审核
     */
    private Integer auditStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
