package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("运营人员登录模型")
@Data
public class LoginForWorkReqDTO {

    /**
     * 运营人员账号
     */
    @ApiModelProperty(value = "服务人员账号",required = true)
    private String phone;
    /**
     * 登录密码
     */
    @ApiModelProperty(value = "登录密码,机构用户必填",required = false)
    private String password;

    @ApiModelProperty("验证码，普通服务人员必填")
    private String veriryCode;

    /**
     * 登录用户类型
     */
    @ApiModelProperty("服务人员类型,2.普通服务人员，3：机构用户")
    private Integer userType;
}
