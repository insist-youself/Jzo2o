package com.jzo2o.market.service;

import com.jzo2o.api.market.dto.request.CouponUseBackReqDTO;
import com.jzo2o.api.market.dto.request.CouponUseReqDTO;
import com.jzo2o.api.market.dto.response.AvailableCouponsResDTO;
import com.jzo2o.api.market.dto.response.CouponUseResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.market.model.domain.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.market.model.dto.request.CouponOperationPageQueryReqDTO;
import com.jzo2o.market.model.dto.request.SeizeCouponReqDTO;
import com.jzo2o.market.model.dto.response.CouponInfoResDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-16
 */
public interface ICouponService extends IService<Coupon> {

    /**
     * 运营端查询同一个活动的优惠券
     * @param couponOperationPageQueryReqDTO
     * @return
     */
    PageResult<CouponInfoResDTO> queryForPageOfOperation(CouponOperationPageQueryReqDTO couponOperationPageQueryReqDTO);

    /**
     * 滚动查询用户优惠券列表
     *
     * @param lastId 上一批查询最后一条优惠券的id
     * @param userId 查询用户的id
     * @param status 优惠券状态
     * @return 优惠券列表
     */
    List<CouponInfoResDTO> queryForList(Long lastId, Long userId, Integer status);



    /**
     * 作废指定活动未使用的优惠券
     * @param activityId
     */
    void revoke(Long activityId);

    /**
     * 统计获取优惠券领取数量
     *
     * @param activityId
     * @return
     */
    Integer countReceiveNumByActivityId(Long activityId);

    /**
     * 过期优惠券处理
     */
    void processExpireCoupon();
}
