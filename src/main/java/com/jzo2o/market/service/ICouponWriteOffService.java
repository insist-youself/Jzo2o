package com.jzo2o.market.service;

import com.jzo2o.market.model.domain.CouponWriteOff;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 优惠券核销表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-22
 */
public interface ICouponWriteOffService extends IService<CouponWriteOff> {


    /**
     * 根据获取活动id统计核销量
     * @param activityId
     * @return
     */
    Integer countByActivityId(Long activityId);
}
