package com.jzo2o.orders.base.utils;

import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.NumberUtils;

import java.time.LocalDateTime;

public class ServeTimeUtils {

    /**
     * 获取服务时间，用来处理抢单和派单的时间冲突问题
     *
     * @param serveStartTime
     * @return
     */
    public static int getServeTimeInt(LocalDateTime serveStartTime) {
        return NumberUtils.parseInt(DateUtils.format(serveStartTime, "yyyyMMddHH"));
    }

    public static void main(String[] args) {
        long number = 2023082400000000001L;
        System.out.println(number % 10000000000L % 150000);
    }
}
