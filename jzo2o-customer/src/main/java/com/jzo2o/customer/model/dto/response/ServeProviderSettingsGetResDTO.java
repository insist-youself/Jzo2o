package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("服务范围设置")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServeProviderSettingsGetResDTO {
    /**
     * 服务所在城市编码
     */
    @ApiModelProperty("服务所在城市编码")
    private String cityCode;
    /**
     * 服务所在城市名称
     */
    @ApiModelProperty("服务所在城市名称")
    private String cityName;
    /**
     * 服务范围经纬度，格式经度,纬度
     */
    @ApiModelProperty("服务范围经纬度，格式经度,纬度")
    private String location;
    /**
     * 意向接单范围
     */
    @ApiModelProperty("意向接单范围")
    private String intentionScope;

    /**
     * 是否有技能
     */
    @ApiModelProperty("是否有技能")
    private Boolean haveSkill;

    /**
     * 是否开启接单
     */
    @ApiModelProperty("是否可以接单")
    private Boolean canPickUp;
}
