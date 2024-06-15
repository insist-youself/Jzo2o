package com.jzo2o.market.utils;

import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.market.enums.ActivityTypeEnum;
import com.jzo2o.market.model.domain.Coupon;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 优惠券相关工具
 */
public class CouponUtils {
    /**
     * 计算优惠金额
     *
     * @param coupon 优惠券
     * @param totalAmount 订单金额
     * @return
     */
    public static BigDecimal calDiscountAmount(Coupon coupon, BigDecimal totalAmount) {
        // 满减优惠
        if (ActivityTypeEnum.AMOUNT_DISCOUNT.equals(coupon.getType())) {
            //满减金额
            BigDecimal amountCondition = coupon.getAmountCondition();
            //优惠金额
            BigDecimal discountAmount = coupon.getDiscountAmount();
            //如果订单金额小于满减金额不满足优惠条件
            if(totalAmount.compareTo(amountCondition) < 0 ){
                return BigDecimal.ZERO;
            }
            return discountAmount;
        } else {//折扣优惠
            //折扣率
            Integer discountRate = coupon.getDiscountRate();
            //折扣率非法
            if(discountRate>=100 || discountRate<=0)  return BigDecimal.ZERO;
            //优惠率
            BigDecimal rate = new BigDecimal(100 - coupon.getDiscountRate()).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
            //优惠金额
            BigDecimal discountAmount = totalAmount.multiply(rate);
            return  discountAmount;
        }
    }

}
