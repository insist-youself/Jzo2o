package com.jzo2o.api.customer.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新用户请求
 *
 * @author itcast
 * @create 2023/7/17 17:10
 **/
@Data
@ApiModel("更新用户请求")
public class CommonUserUpdateReqDTO {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
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
