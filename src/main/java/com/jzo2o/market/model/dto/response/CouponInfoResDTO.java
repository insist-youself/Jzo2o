package com.jzo2o.market.model.dto.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 * @since 2023-09-16
 */
@Data
public class CouponInfoResDTO implements Serializable {

    @ApiModelProperty(value = "优惠券id",required = true)
    private Long id;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "活动名称",required = true)
    private String name;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户手机号")
    private String userPhone;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id",required = true)
    private Long activityId;

    @ApiModelProperty(value = "使用类型，1：满减，2：折扣",required = true)
    private Integer type;

    /**
     * 折扣
     */
    @ApiModelProperty(value = "折扣",required = false)
    private Integer discountRate;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额",required = false)
    private BigDecimal discountAmount;

    /**
     * 满减金额
     */
    @ApiModelProperty(value = "满减条件,0:表示无门槛",required = true)
    private BigDecimal amountCondition;

    /**
     * 有效期
     */
    @ApiModelProperty("优惠券过期时间")
    private LocalDateTime validityTime;

    @ApiModelProperty("使用时间")
    private LocalDateTime useTime;

    /**
     * 优惠券状态，1:未使用，2:已使用，3:已过期
     */
    @ApiModelProperty("优惠券状态，1:未使用，2:已使用，3:已过期")
    private Integer status;

    /**
     * 订单id
     */
    private String ordersId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",required = true)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间",required = true)
    private LocalDateTime updateTime;

}
