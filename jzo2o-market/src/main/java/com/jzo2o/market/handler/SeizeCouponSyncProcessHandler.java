package com.jzo2o.market.handler;

import com.jzo2o.api.customer.CommonUserApi;
import com.jzo2o.api.customer.dto.response.CommonUserResDTO;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.IdUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.market.enums.CouponStatusEnum;
import com.jzo2o.market.model.domain.Activity;
import com.jzo2o.market.model.domain.Coupon;
import com.jzo2o.market.model.dto.response.ActivityInfoResDTO;
import com.jzo2o.market.service.IActivityService;
import com.jzo2o.market.service.ICouponService;
import com.jzo2o.redis.handler.SyncProcessHandler;
import com.jzo2o.redis.model.SyncMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.jzo2o.market.constants.RedisConstants.RedisKey.COUPON_SEIZE_SYNC_QUEUE_NAME;

/**
 * 抢单成功同步任务
 * @author linger
 * @date 2024/6/17 21:16
 */


@Component(COUPON_SEIZE_SYNC_QUEUE_NAME)
@Slf4j
public class SeizeCouponSyncProcessHandler implements SyncProcessHandler<Object> {

    @Resource
    private IActivityService activityService;

    @Resource
    private CommonUserApi commonUserApi;

    @Resource
    private ICouponService couponService;

    @Override
    public void batchProcess(List<SyncMessage<Object>> multiData) {
        throw new RuntimeException("不支持批量处理");
    }

    /**
     * signleData key activityId, value 抢单用户id
     * @param singleData
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void singleProcess(SyncMessage<Object> singleData) {
        log.info("获取要同步的数据： {}",singleData);
        //用户id
        long userId = NumberUtils.parseLong(singleData.getKey());
        //活动id
        long activityId = NumberUtils.parseLong(singleData.getValue().toString());
        log.info("userId={},activity={}",userId,activityId);
        //向优惠券表插入数据
        Activity activity = activityService.getById(activityId);
        CommonUserResDTO commonUserResDTO = commonUserApi.findById(userId);

        Coupon coupon = new Coupon();
        coupon.setId(IdUtils.getSnowflakeNextId());
        coupon.setName(activity.getName());
        coupon.setUserId(userId);
        coupon.setUserName(commonUserResDTO.getNickname());
        coupon.setUserPhone(commonUserResDTO.getPhone());
        coupon.setActivityId(activityId);
        coupon.setType(activity.getType());
        coupon.setDiscountAmount(activity.getDiscountAmount());
        coupon.setDiscountRate(activity.getDiscountRate());
        coupon.setAmountCondition(activity.getAmountCondition());
        coupon.setValidityTime(DateUtils.now().plusDays(activity.getValidityDays()));
        coupon.setStatus(CouponStatusEnum.NO_USE.getStatus());

        couponService.save(coupon);

        // 扣减数据库表中的库存
        activityService.deductStock(activityId);
    }
}
