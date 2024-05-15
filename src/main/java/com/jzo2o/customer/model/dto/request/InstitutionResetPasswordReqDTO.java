package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("机构密码重置接口")
public class InstitutionResetPasswordReqDTO {
    @ApiModelProperty(value = "新密码",required = true)
    private String password;

    @ApiModelProperty(value = "手机号",required = true)
    private String phone;

    @ApiModelProperty(value = "短信验证码",required = true)
    @NotNull(message = "验证码错误，请重新输入")
    private String verifyCode;

}
