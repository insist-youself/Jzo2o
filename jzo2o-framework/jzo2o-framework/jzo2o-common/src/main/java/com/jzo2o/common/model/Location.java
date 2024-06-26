package com.jzo2o.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 经纬度
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    /**
     * 经度
     */
    private Double lon;

    /**
     * 纬度
     */
    private Double lat;

}
