package com.jzo2o.customer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 机构认证信息更新模型
 *
 * @author itcast
 * @create 2023/9/6 10:49
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgencyCertificationUpdateDTO {
    /**
     * 机构id
     */
    private Long id;

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
     * 认证状态
     */
    private Integer certificationStatus;

    /**
     * 认证时间
     */
    private LocalDateTime certificationTime;
}
