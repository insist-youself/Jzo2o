package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("登录结果")
@NoArgsConstructor
@AllArgsConstructor
public class LoginResDTO {

    @ApiModelProperty("运营端访问token")
    private String token;
}
