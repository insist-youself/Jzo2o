package com.jzo2o.thirdparty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地图定位信息
 *
 * @author itcast
 * @create 2023/7/11 09:31
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapLocationDTO {
    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 详细地址
     */
    private String fullAddress;
}
