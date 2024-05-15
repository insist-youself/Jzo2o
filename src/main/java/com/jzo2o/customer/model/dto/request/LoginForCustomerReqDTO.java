package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author itcast
 */
@Data
@ApiModel("微信小程序登录模型")
public class LoginForCustomerReqDTO {
    /**
     * 微信授权code
     */
    @ApiModelProperty(value = "微信授权code", required = true)
    private String code;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;
}
