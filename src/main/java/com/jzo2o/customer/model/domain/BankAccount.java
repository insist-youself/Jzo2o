package com.jzo2o.customer.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务人员/机构银行账户表
 * </p>
 *
 * @author linger
 * @since 2024-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bank_account")
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 开户证明
     */
    private String accountCertification;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 类型，2：服务人员，3：服务机构
     */
    private Integer type;

    /**
     * 网点
     */
    private String branch;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 户名
     */
    private String name;

    /**
     * 银行账号
     */
    private String account;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否已删除，0：未删除，1：已删除
     */
    private Integer isDeleted;


}
