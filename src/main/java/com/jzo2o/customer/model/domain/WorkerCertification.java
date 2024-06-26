package com.jzo2o.customer.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务人员认证信息表
 * </p>
 *
 * @author itcast
 * @since 2023-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("worker_certification")
public class WorkerCertification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务人员id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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
     * 认证状态，0：初始态，1：认证中，2：认证成功，3认证失败
     */
    private Integer certificationStatus;

    /**
     * 认证时间
     */
    private LocalDateTime certificationTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
