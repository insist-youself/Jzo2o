package com.jzo2o.api.operation.utils;

import com.jzo2o.common.constants.UserType;

import java.math.BigDecimal;

/**
 * 区域相关的工具
 */
public class RegionUtils {

    /**
     * 根据用户计算拥挤比例
     *
     * @param userType
     * @param institutionStaffCommissionRate
     * @param serveStaffCommissionRate
     * @return
     */
    public static BigDecimal calculateCommissionRate(Integer userType, Double institutionStaffCommissionRate, Double serveStaffCommissionRate) {
        if (userType.equals(UserType.INSTITUTION) && institutionStaffCommissionRate != null) {
            return new BigDecimal(institutionStaffCommissionRate / 100);
        } else if (userType.equals(UserType.WORKER) && serveStaffCommissionRate != null) {
            return new BigDecimal(serveStaffCommissionRate / 100);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
