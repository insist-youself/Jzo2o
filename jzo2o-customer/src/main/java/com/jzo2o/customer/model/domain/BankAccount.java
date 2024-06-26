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
 * 银行账户
 * </p>
 *
 * @author itcast
 * @since 2023-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bank_account")
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务人员/机构id
     */
    private Long id;

    /**
     * 类型，2：服务人员，3：服务机构
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;

    /**
     * 银行名称
     */
    private String bankName;

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
     * 网点
     */
    private String branch;

    /**
     * 银行账号
     */
    private String account;

    /**
     * 开户证明
     */
    private String accountCertification;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
