package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 银行账户响应体
 * </p>
 *
 * @author itcast
 * @since 2023-09-06
 */
@Data
@ApiModel("银行账户响应体")
public class BankAccountResDTO implements Serializable {

    /**
     * 服务人员/机构id
     */
    @ApiModelProperty("服务人员/机构id")
    private Long id;

    /**
     * 类型，2：服务人员，3：服务机构
     */
    @ApiModelProperty("类型，2：服务人员，3：服务机构")
    private Integer type;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 银行名称
     */
    @ApiModelProperty("银行名称")
    private String bankName;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty("区")
    private String district;

    /**
     * 网点
     */
    @ApiModelProperty("网点")
    private String branch;

    /**
     * 银行账号
     */
    @ApiModelProperty("银行账号")
    private String account;

    /**
     * 开户证明
     */
    @ApiModelProperty("开户证明")
    private String accountCertification;

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
