package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("短信验证码下发接口")
public class SmsCodeSendReqDTO {
    @ApiModelProperty(value = "下发手机号码",required = true)
    private String phone;
    @ApiModelProperty("业务类型，1：机构注册，2：机构忘记密码,3:服务人员登录")
    private String bussinessType;
}
