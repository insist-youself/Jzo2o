package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务信息设置
 */
@ApiModel("服务信息设置状态")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ServeSettingsStatusResDTO {
    /**
     * 认证状态，0：初始态，1：认证中，2：认证成功，3认证失败
     */
    @ApiModelProperty("认证状态，0：初始态，1：认证中，2：认证成功，3认证失败")
    private Integer certificationStatus;

//    @ApiModelProperty("是否完成认证")
//    private Boolean authed;

    /**
     * 是否完成服务信息设置
     */
    @ApiModelProperty("是否完成服务信息设置")
    private Boolean serveSkillSetted;
    /**
     * 是否设置服务范文设置
     */
    @ApiModelProperty("是否设置服务范围")
    private Boolean serveScopeSetted;
    /**
     * 是否开启接单
     */
    @ApiModelProperty("是否开启接单")
    private Boolean canPickUp;
    /**
     * 是否设置过接单
     */
//    @ApiModelProperty("是否设置过接单")
//    private Boolean pickUpSetted;
    /**
     * 设置状态，0：未完成所有设置、认证
     */
    @ApiModelProperty("初次设置状态，0：未完成设置、认证，1：已经完成所有设置、认证")
    private Integer settingsStatus;
}
