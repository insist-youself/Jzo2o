package com.jzo2o.customer.model.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 机构认证信息表
 * </p>
 *
 * @author itcast
 * @since 2023-09-06
 */
@Data
public class CertificationStatusDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构id或服务端id
     */
    private Long id;


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
