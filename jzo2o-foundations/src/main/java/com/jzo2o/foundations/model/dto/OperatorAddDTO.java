package com.jzo2o.foundations.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author itcast
 */
@Data
@ApiModel("运营人员新增模型")
public class OperatorAddDTO {
    @ApiModelProperty("账号")
    private String username;

    @ApiModelProperty("运营人员姓名")
    private String name;

    @ApiModelProperty("密码")
    private String password;
}
