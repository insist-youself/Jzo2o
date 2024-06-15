package com.jzo2o.market.model.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券配置id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 优惠券名称，可以和活动名称保持一致
     */
    private String name;

    /**
     * 使用类型，1：满减，2：折扣
     */
    private Integer type;

    /**
     * 使用条件，0：表示无门槛，其他值：最低消费金额
     */
    private BigDecimal amountCondition;

    /**
     * 折扣率，折扣类型的折扣率，8折就是存80
     */
    private Integer discountRate;

    /**
     * 优惠金额，满减或无门槛的优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 优惠券有效期天数
     */
    private Integer validityDays;

    /**
     * 发放开始时间
     */
    private LocalDateTime distributeStartTime;

    /**
     * 发放结束时间
     */
    private LocalDateTime distributeEndTime;

    /**
     * 优惠券配置状态，1：待生效，2：进行中，3：已失效
     */
    private Integer status;

    /**
     * 发放数量，0：表示无限量，其他正数表示最大发放量
     */
    private Integer totalNum;
    /**
     * 库存数量
     */
    private Integer stockNum;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;


}
