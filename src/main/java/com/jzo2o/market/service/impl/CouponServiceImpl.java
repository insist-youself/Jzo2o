package com.jzo2o.market.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.*;
import com.jzo2o.market.mapper.CouponMapper;
import com.jzo2o.market.model.domain.Coupon;
import com.jzo2o.market.model.dto.request.CouponOperationPageQueryReqDTO;
import com.jzo2o.market.model.dto.response.CouponInfoResDTO;
import com.jzo2o.market.service.IActivityService;
import com.jzo2o.market.service.ICouponService;
import com.jzo2o.market.service.ICouponUseBackService;
import com.jzo2o.market.service.ICouponWriteOffService;
import com.jzo2o.mysql.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-16
 */
@Service
@Slf4j
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {

    @Resource(name = "seizeCouponScript")
    private DefaultRedisScript<String> seizeCouponScript;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private IActivityService activityService;

    @Resource
    private ICouponUseBackService couponUseBackService;

    @Resource
    private ICouponWriteOffService couponWriteOffService;


    /**
     * 根据活动ID查询优惠券领取记录
     * @param couponOperationPageQueryReqDTO 运营端优惠券查询模型
     * @return 优惠券详情信息
     */
    @Override
    public PageResult<CouponInfoResDTO> queryByActivityId(CouponOperationPageQueryReqDTO couponOperationPageQueryReqDTO) {
        // 1.数据校验
        if (ObjectUtils.isNull(couponOperationPageQueryReqDTO.getActivityId())) {
            throw new BadRequestException("请指定活动");
        }
        // 2.数据查询
        // 分页 排序
        Page<Coupon> couponQueryPage = PageUtils.parsePageQuery(couponOperationPageQueryReqDTO, Coupon.class);
        // 查询条件
        LambdaQueryWrapper<Coupon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Coupon::getActivityId, couponOperationPageQueryReqDTO.getActivityId());
        // 查询数据
        Page<Coupon> couponPage = baseMapper.selectPage(couponQueryPage, lambdaQueryWrapper);

        // 3.数据转化，并返回
        return PageUtils.toPage(couponPage, CouponInfoResDTO.class);
    }

    /**
     * 用户查询自己领取的优惠券
     *
     * @param userId 用户id
     * @param status 优惠券状态，1：未使用，2：已使用，3：已过期
     * @param lastId 上一次查询最后一张优惠券id
     * @return 优惠券详情
     */
    @Override
    public List<CouponInfoResDTO> queryMyCoupon(Long userId, Long status, Long lastId) {
        // 判断 status 是否正确
        if (status < 1 || status > 3) {
            throw new BadRequestException("优惠券状态不存在");
        }
        // 查询对应用户的优惠券 （）
        List<Coupon> couponList = lambdaQuery()
                .eq(Coupon::getUserId, userId)
                .eq(Coupon::getStatus, status)
                .lt(ObjectUtils.isNotEmpty(lastId), Coupon::getId, lastId)
                .orderByDesc(Coupon::getId)
                .last("limit 10")
                .list();
        if (CollUtils.isEmpty(couponList)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyToList(couponList, CouponInfoResDTO.class);
    }
}
