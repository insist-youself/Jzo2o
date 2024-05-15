package com.jzo2o.customer.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务人员认证更新模型
 *
 * @author itcast
 * @create 2023/9/6 11:37
 **/
@Data
public class WorkerCertificationUpdateDTO {

    /**
     * 服务人员id
     */
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
}
