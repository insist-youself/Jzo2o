package com.jzo2o.market.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("抢券列表信息")
public class SeizeCouponInfoResDTO implements Serializable {
    @ApiModelProperty("活动id")
    private Long id;
    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty("优惠券类型，1：满减，2：折扣")
    private Integer type;
    @ApiModelProperty("满减限额，0：表示无门槛，其他值：最低消费金额")
    private BigDecimal amountCondition;
    @ApiModelProperty("折扣率，折扣类型的折扣率，例如：8,打8折")
    private Integer discountRate;
    @ApiModelProperty("优惠金额，满减或无门槛的优惠金额")
    private BigDecimal discountAmount;
    @ApiModelProperty("发放开始时间")
    private LocalDateTime distributeStartTime;
    @ApiModelProperty("发放结束时间")
    private LocalDateTime distributeEndTime;
    @ApiModelProperty("优惠券配置状态，1：待生效，2：进行中，3：已失效")
    private Integer status;
    @ApiModelProperty("优惠券剩余数量")
    private Integer remainNum;
    @ApiModelProperty("发放数量")
    private Integer totalNum;
    @ApiModelProperty("库存数量")
    private Integer stockNum;
}
