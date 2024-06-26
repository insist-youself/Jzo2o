package com.jzo2o.thirdparty.core.map;

import com.jzo2o.thirdparty.dto.MapLocationDTO;

/**
 * @author itcast
 */
public interface MapService {
    /**
     * 根据地址获取经纬度坐标
     *
     * @param address 地址
     * @return 经纬度坐标
     */
    String getLocationByAddress(String address);

    /**
     * 根据经纬度获取城市编码
     *
     * @param location 经纬度，经度在前，纬度在后
     * @return 城市编码
     */
    MapLocationDTO getCityCodeByLocation(String location);
}
