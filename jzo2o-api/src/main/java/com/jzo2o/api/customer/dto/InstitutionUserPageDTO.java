package com.jzo2o.api.customer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InstitutionUserPageDTO {
    @ApiModelProperty("机构用户id")
    private Long id;
    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("上次活跃时间")
    private LocalDateTime lastLoginTime;
    @ApiModelProperty("账号状态")
    private Integer status;
    @ApiModelProperty("认证状态")
    private Integer authStatus;
}
