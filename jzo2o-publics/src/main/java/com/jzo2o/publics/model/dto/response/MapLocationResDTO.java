package com.jzo2o.publics.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 地图定位响应信息
 *
 * @author itcast
 * @create 2023/7/10 10:04
 **/
@Data
@ApiModel("地图定位响应信息")
public class MapLocationResDTO {

    /**
     * 城市编码
     */
    @ApiModelProperty("城市编码")
    private String cityCode;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty("区")
    private String district;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String fullAddress;
}
