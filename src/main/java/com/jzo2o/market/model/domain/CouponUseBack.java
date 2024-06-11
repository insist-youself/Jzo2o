package com.jzo2o.market.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券使用回退记录
 * </p>
 *
 * @author itcast
 * @since 2023-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CouponUseBack implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回退记录id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 回退时间
     */
    private LocalDateTime useBackTime;

    /**
     * 核销时间
     */
    private LocalDateTime writeOffTime;


}
