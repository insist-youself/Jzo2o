package com.jzo2o.orders.base.utils;

import com.jzo2o.common.utils.NumberUtils;

public class RedisUtils {

    /**
     * 获取城市编码最后以为数字
     *
     * @param cityCode
     * @return
     */
    public static int getCityIndex(String cityCode) {
        return NumberUtils.parseInt(cityCode) % 10;
    }
}
