package com.jzo2o.foundations.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("运营人员登录模型")
@Data
public class LoginReqDTO {

    /**
     * 运营人员账号
     */
    @ApiModelProperty("运营人员账号")
    private String username;
    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    private String password;
}
