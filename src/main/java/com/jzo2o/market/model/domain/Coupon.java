package com.jzo2o.market.model.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 * @since 2023-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券的拥有者
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 使用类型，1：满减，2：折扣
     */
    private Integer type;

    /**
     * 折扣
     */
    private Integer discountRate;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 满减金额
     */
    private BigDecimal amountCondition;

    /**
     * 有效期
     */
    private LocalDateTime validityTime;

    /**
     * 使用时间
     */
    private LocalDateTime useTime;
    /**
     * 优惠券状态，1:未使用，2:已使用，3:已过期
     */
    private Integer status;

    /**
     * 订单id
     */
    private String ordersId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;


}
