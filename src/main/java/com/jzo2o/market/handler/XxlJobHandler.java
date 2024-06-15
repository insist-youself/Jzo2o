package com.jzo2o.market.handler;

import com.jzo2o.market.service.IActivityService;
import com.jzo2o.market.service.ICouponService;
import com.jzo2o.redis.annotations.Lock;
import com.jzo2o.redis.constants.RedisSyncQueueConstants;
import com.jzo2o.redis.sync.SyncManager;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.jzo2o.market.constants.RedisConstants.Formatter.*;
import static com.jzo2o.market.constants.RedisConstants.RedisKey.COUPON_SEIZE_SYNC_QUEUE_NAME;

@Component
@Slf4j
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
     * 每分钟执行一次
     */
    @XxlJob("updateActivityStatus")
    public void updateActivitySatus(){
        log.info("定时修改活动状态...");
        try {
            activityService.updateStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 已领取优惠券自动过期任务
     * 每小时执行一次
     */
    @XxlJob("processExpireCoupon")
    public void processExpireCoupon() {
        log.info("已领取优惠券自动过期任务...");
        try {
            couponService.processExpireCoupon();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
