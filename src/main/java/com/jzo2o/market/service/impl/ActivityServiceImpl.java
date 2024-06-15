package com.jzo2o.market.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.*;
import com.jzo2o.market.constants.TabTypeConstants;
import com.jzo2o.market.enums.ActivityStatusEnum;
import com.jzo2o.market.enums.ActivityTypeEnum;
import com.jzo2o.market.enums.CouponStatusEnum;
import com.jzo2o.market.mapper.ActivityMapper;
import com.jzo2o.market.model.domain.Activity;
import com.jzo2o.market.model.domain.Coupon;
import com.jzo2o.market.model.dto.request.ActivityQueryForPageReqDTO;
import com.jzo2o.market.model.dto.request.ActivitySaveReqDTO;
import com.jzo2o.market.model.dto.response.ActivityInfoResDTO;
import com.jzo2o.market.model.dto.response.SeizeCouponInfoResDTO;
import com.jzo2o.market.service.IActivityService;
import com.jzo2o.market.service.ICouponService;
import com.jzo2o.market.service.ICouponWriteOffService;
import com.jzo2o.mysql.utils.PageUtils;
import org.aspectj.util.TypeSafeEnum;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jzo2o.market.constants.RedisConstants.RedisKey.*;
import static com.jzo2o.market.enums.ActivityStatusEnum.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-16
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {
    private static final int MILLION = 1000000;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ICouponService couponService;

    @Resource
    private ICouponWriteOffService couponWriteOffService;


    @Override
    public void saveOrUpActivity(ActivitySaveReqDTO activitySaveReqDTO) {
        // 判断请求是否为 null
        if (ObjectUtils.isNull(activitySaveReqDTO)) {
            throw new CommonException("请求参数为空，请求失败");
        }


        if (activitySaveReqDTO.getType() == ActivityTypeEnum.AMOUNT_DISCOUNT.getType()) {
            // 如果满减：1、折扣金额必须输入2、折扣金额必须大于0的整数
            BigDecimal discountAmount = activitySaveReqDTO.getDiscountAmount();
            if (ObjectUtils.isNull(discountAmount)) {
                throw new CommonException("折扣金额必须输入");
            }
            if (discountAmount.intValue() <= 0) {
                throw new CommonException("折扣金额必须大于0的整数");
            }
        } else {
            //如果是折扣：1、折扣比例必须输入 2、折扣比例必须大于0，小于100的整数
            Integer discountRate = activitySaveReqDTO.getDiscountRate();
            if (ObjectUtils.isNull(discountRate)) {
                throw new CommonException("折扣比例必须输入");
            }
            if (discountRate <= 0 || discountRate >= 100) {
                throw new CommonException("折扣比例必须大于0，小于100的整数");
            }
        }
        // 满额限制必须大于等于0
        if (activitySaveReqDTO.getAmountCondition().intValue() < 0) {
            throw new CommonException("满额限制必须大于等于0");
        }
        // 发放时间开始时间至少设置在距离当前时间的1个小时之后
        if (activitySaveReqDTO.getDistributeStartTime().minusHours(1).isBefore(LocalDateTime.now())) {
            throw new CommonException("发放时间开始时间至少设置在距离当前时间的1个小时之后");
        }
        Activity activity = BeanUtils.copyBean(activitySaveReqDTO, Activity.class);
        activity.setStockNum(activitySaveReqDTO.getTotalNum());
        activity.setStatus(NO_DISTRIBUTE.getStatus());
        // 根据 id 判断是新增还是修改
        if(ObjectUtils.isNotNull(activitySaveReqDTO.getId())) {
            Activity activity1 = getById(activitySaveReqDTO.getId());
            if(ObjectUtils.isNull(activity1)) {
                throw new CommonException("待更新的优惠券活动不存在");
            }
            boolean update = updateById(activity);
            if(!update) {
                throw new CommonException("更新失败");
            }
        } else {
            // 新增优惠券活动
            baseMapper.insert(activity);
        }
    }

    /**
     * 分页查询优惠券活动
     * @param activityQueryForPageReqDTO ActivityQueryForPageReqDTO
     * @return 活动分页字段模型
     */
    @Override
    public PageResult<ActivityInfoResDTO> queryForPage(ActivityQueryForPageReqDTO activityQueryForPageReqDTO) {
        Page<Activity> page = PageUtils.parsePageQuery(activityQueryForPageReqDTO, Activity.class);
        // 构建查询条件
        LambdaQueryWrapper<Activity> lambdaQueryWrapper = new LambdaQueryWrapper<Activity>()
                .eq(ObjectUtils.isNotEmpty(activityQueryForPageReqDTO.getId()), Activity::getId, activityQueryForPageReqDTO.getId())
                .eq(ObjectUtils.isNotEmpty(activityQueryForPageReqDTO.getType()), Activity::getType, activityQueryForPageReqDTO.getType())
                .eq(ObjectUtils.isNotEmpty(activityQueryForPageReqDTO.getName()), Activity::getName, activityQueryForPageReqDTO.getName())
                .eq(ObjectUtils.isNotEmpty(activityQueryForPageReqDTO.getStatus()), Activity::getStatus, activityQueryForPageReqDTO.getStatus());
        Page<Activity> serveTypePage = baseMapper.selectPage(page, lambdaQueryWrapper);

        return PageUtils.toPage(serveTypePage, ActivityInfoResDTO.class);
    }

    /**
     * 查询优惠券活动详情
     * @param id 活动id
     * @return 活动分页字段模型
     */
    @Override
    public ActivityInfoResDTO queryByActivityId(Long id) {
        Activity activity = getById(id);
        if (ObjectUtils.isNull(activity)) {
            throw new CommonException("对应优惠活动不存在");
        }
        return BeanUtils.copyBean(activity, ActivityInfoResDTO.class);
    }

    /**
     *  撤销活动
     * @param id 活动id
     */
    @Override
    public void revokeById(Long id) {
        // 查询对应id的优惠活动是否存在
        Activity activity = getById(id);
        if (ObjectUtils.isNull(activity)) {
            throw new BadRequestException("对应优惠活动不存在");
        }
        // 活动状态更改为作废
        activity.setStatus(VOIDED.getStatus());
        boolean update = updateById(activity);
        if(!update) {
            throw new CommonException("更新优惠活动状态失败");
        }
        // 查询所有抢到本活动优惠券的、状态为未使用的优惠券，更改状态为已失效
        update = couponService.lambdaUpdate()
                .eq(Coupon::getActivityId, id)
                .eq(Coupon::getStatus, CouponStatusEnum.NO_USE.getStatus())
                .set(Coupon::getStatus, CouponStatusEnum.INVALID.getStatus())
                .update();
        if(!update) {
            throw new CommonException("更新优惠活动状态失败");
        }
    }
}
