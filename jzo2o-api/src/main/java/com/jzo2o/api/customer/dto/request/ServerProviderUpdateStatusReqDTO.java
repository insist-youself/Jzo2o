package com.jzo2o.api.customer.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务人员/机构更新状态请求
 *
 * @author itcast
 * @create 2023/7/18 21:34
 **/
@Data
@ApiModel("服务人员/机构更新状态请求")
public class ServerProviderUpdateStatusReqDTO {
    /**
     * 服务人员/机构id
     */
    @ApiModelProperty(value = "服务人员/机构id", required = true)
    private Long id;

    /**
     * 状态，0：正常，1：冻结
     */
    @ApiModelProperty(value = "状态，0：正常，1：冻结", required = true)
    private Integer status;

    /**
     * 账号冻结原因
     */
    @ApiModelProperty("账号冻结原因")
    private String accountLockReason;
}
