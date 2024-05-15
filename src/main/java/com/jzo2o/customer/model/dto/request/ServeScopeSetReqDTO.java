package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 86188
 */
@Data
@ApiModel("服务范围设置模型")
public class ServeScopeSetReqDTO {
    /**
     * 城市编码
     */
    @ApiModelProperty(value = "城市编码", required = true)
    @NotNull(message="请求失败")
    private String cityCode;

    @ApiModelProperty(value = "城市名称", required = true)
    @NotNull(message="请求失败")
    private String cityName;
    /**
     * 坐标经纬度，例如经度,纬度
     */
    @ApiModelProperty(value = "坐标经纬度，例如经度,纬度", required = true)
    @NotNull(message="请求失败")
    private String location;
    /**
     * 意向接单范围
     */
    @ApiModelProperty(value = "意向接单范围", required = true)
    @NotNull(message="请求失败")
    private String intentionScope;
}
