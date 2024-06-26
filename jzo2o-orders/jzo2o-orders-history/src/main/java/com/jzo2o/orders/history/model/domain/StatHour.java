package com.jzo2o.orders.history.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 小时统计表
 * </p>
 *
 * @author itcast
 * @since 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class StatHour implements Serializable {

    private static final long serialVersionUID = 1L;

    public StatHour(Long id) {
        this.id = id;
        this.statTime = id.intValue();
        this.effectiveOrderNum = 0;
        this.cancelOrderNum = 0;
        this.closeOrderNum = 0;
        this.effectiveOrderTotalAmount = BigDecimal.ZERO;
        this.realPayAveragePrice = BigDecimal.ZERO;
        this.totalOrderNum = 0;
    }

    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 统计日期，格式：yyyyMMddHH
     */
    private Integer statTime;

    /**
     * 有效订单数
     */
    private Integer effectiveOrderNum;

    /**
     * 取消订单数
     */
    private Integer cancelOrderNum;

    /**
     * 关闭订单数
     */
    private Integer closeOrderNum;

    /**
     * 有效总金额
     */
    private BigDecimal effectiveOrderTotalAmount;

    /**
     * 实付订单均价
     */
    private BigDecimal realPayAveragePrice;

    /**
     * 订单总数
     */
    private Integer totalOrderNum;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
