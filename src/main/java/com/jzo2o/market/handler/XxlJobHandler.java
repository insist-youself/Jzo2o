package com.jzo2o.market.handler;

import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.market.enums.ActivityStatusEnum;
import com.jzo2o.market.enums.CouponStatusEnum;
import com.jzo2o.market.model.domain.Activity;
import com.jzo2o.market.model.domain.Coupon;
import com.jzo2o.market.service.IActivityService;
import com.jzo2o.market.service.ICouponService;
import com.jzo2o.redis.annotations.Lock;
import com.jzo2o.redis.constants.RedisSyncQueueConstants;
import com.jzo2o.redis.sync.SyncManager;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.jzo2o.market.constants.RedisConstants.Formatter.*;
import static com.jzo2o.market.constants.RedisConstants.RedisKey.COUPON_SEIZE_SYNC_QUEUE_NAME;
import static com.jzo2o.market.enums.ActivityStatusEnum.DISTRIBUTING;
import static com.jzo2o.market.enums.ActivityStatusEnum.NO_DISTRIBUTE;

@Component
public class XxlJobHandler {

    @Resource
    private SyncManager syncManager;

    @Resource
    private IActivityService activityService;

    @Resource
    private ICouponService couponService;

    /**
     * 活动状态修改，
     * 1.活动进行中状态修改
     * 2.活动已失效状态修改
     * 1分钟一次
     */
    @XxlJob("updateActivityStatus")
    public void updateActivitySatus(){
        LocalDateTime now = DateUtils.now();
        // 活动进行中状态修改
        activityService.lambdaUpdate()
                .eq(Activity::getStatus, NO_DISTRIBUTE.getStatus())
                .le(Activity::getDistributeStartTime, now)
                .ge(Activity::getDistributeEndTime, now)
                .set(Activity::getStatus, DISTRIBUTING.getStatus())
                .update();
        // 活动已失效状态修改
        activityService.lambdaUpdate()
//                .eq(Activity::getStatus, ActivityStatusEnum.NO_DISTRIBUTE.getStatus())
//                .eq(Activity::getStatus, ActivityStatusEnum.DISTRIBUTING.getStatus())
                .in(Activity::getStatus, Arrays.asList(NO_DISTRIBUTE.getStatus(), DISTRIBUTING.getStatus()))
                .lt(Activity::getDistributeEndTime, now)
                .set(Activity::getStatus, ActivityStatusEnum.LOSE_EFFICACY.getStatus())
                .update();
    }

    /**
     * 已领取优惠券自动过期任务
     */
    @XxlJob("processExpireCoupon")
    public void processExpireCoupon() {
        couponService.lambdaUpdate()
                .eq(Coupon::getStatus, CouponStatusEnum.NO_USE.getStatus())
                .le(Coupon::getValidityTime, LocalDateTime.now())
                .set(Coupon::getStatus, CouponStatusEnum.INVALID.getStatus())
                .update();
    }


}
