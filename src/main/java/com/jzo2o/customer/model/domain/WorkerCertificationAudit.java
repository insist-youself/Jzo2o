package com.jzo2o.customer.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务人员认证审核信息表
 * </p>
 *
 * @author linger
 * @since 2024-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("worker_certification_audit")
public class WorkerCertificationAudit implements Serializable {

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
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 身份证正面
     */
    private String frontImg;

    /**
     * 身份证反面
     */
    private String backImg;

    /**
     * 证明资料
     */
    private String certificationMaterial;

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
