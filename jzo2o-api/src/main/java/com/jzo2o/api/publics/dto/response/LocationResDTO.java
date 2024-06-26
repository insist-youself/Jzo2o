package com.jzo2o.api.publics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 经纬度
 *
 * @author itcast
 * @create 2023/8/25 20:01
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationResDTO {
    /**
     * 经纬度
     */
    private String location;
}
