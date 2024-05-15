package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 86188
 */
@Data
@ApiModel("机构注册信息")
public class InstitutionRegisterReqDTO {
    @ApiModelProperty(value = "机构注册手机号",required = true)
    @NotNull(message = "注册手机号不能为空")
    private String phone;
    @ApiModelProperty(value = "注册密码",required = true)
    @NotNull(message = "密码输入格式错误，请重新输入")
    @Size(max = 16, min = 8, message = "密码输入格式错误，请重新输入")
    private String password;
    @ApiModelProperty(value = "短信验证码",required = true)
    @NotNull(message = "验证码错误，请重新输入")
    private String verifyCode;
}
