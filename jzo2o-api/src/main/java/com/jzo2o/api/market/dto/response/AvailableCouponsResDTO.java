package com.jzo2o.api.market.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("优惠券信息")
public class AvailableCouponsResDTO {
    @ApiModelProperty("优惠券id")
    private Long id;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "活动名称",required = true)
    private String name;

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
}
