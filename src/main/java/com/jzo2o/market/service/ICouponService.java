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
     * 根据活动ID查询优惠券领取记录
     * @param couponOperationPageQueryReqDTO 运营端优惠券查询模型
     * @return 优惠券详情信息
     */
    PageResult<CouponInfoResDTO> queryByActivityId(CouponOperationPageQueryReqDTO couponOperationPageQueryReqDTO);

    /**
     * 用户查询自己领取的优惠券
     * @param userId 用户id
     * @param status 优惠券状态，1：未使用，2：已使用，3：已过期
     * @param lastId 上一次查询最后一张优惠券id
     * @return 优惠券详情
     */
    List<CouponInfoResDTO> queryMyCoupon(Long userId, Long status, Long lastId);
}
