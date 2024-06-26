package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 银行账户新增或更新请求体
 * </p>
 *
 * @author itcast
 * @since 2023-09-06
 */
@Data
@ApiModel("银行账户新增或更新请求体")
public class BankAccountUpsertReqDTO implements Serializable {

    /**
     * 服务人员/机构id
     */
    @ApiModelProperty(value = "服务人员/机构id", required = false)
    private Long id;

    /**
     * 类型，2：服务人员，3：服务机构
     */
    @ApiModelProperty(value = "类型，2：服务人员，3：服务机构", required = false)
    private Integer type;

    /**
     * 户名
     */
    @ApiModelProperty(value = "户名", required = true)
    private String name;

    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称", required = true)
    private String bankName;

    /**
     * 省
     */
    @ApiModelProperty(value = "省", required = true)
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市", required = true)
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区", required = true)
    private String district;

    /**
     * 网点
     */
    @ApiModelProperty(value = "网点", required = true)
    private String branch;

    /**
     * 银行账号
     */
    @ApiModelProperty(value = "银行账号", required = true)
    private String account;

    /**
     * 开户证明
     */
    @ApiModelProperty(value = "开户证明", required = true)
    private String accountCertification;
}
