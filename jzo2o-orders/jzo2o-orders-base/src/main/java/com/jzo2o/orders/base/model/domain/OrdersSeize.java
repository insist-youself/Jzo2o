package com.jzo2o.orders.base.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抢单池
 * </p>
 *
 * @author itcast
 * @since 2023-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersSeize implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 服务分类id
     */
    private Long serveTypeId;

    /**
     * 服务名称
     */
    private String serveItemName;

    /**
     * 服务分类名称
     */
    private String serveTypeName;

    /**
     * 服务项id
     */
    private Long serveItemId;

    /**
     * 服务地址
     */
    private String serveAddress;

    /**
     * 服务项目图片
     */
    private String serveItemImg;

    /**
     * 订单总金额
     */
    private BigDecimal ordersAmount;

    /**
     * 服务开始时间
     */
    private LocalDateTime serveStartTime;

    /**
     * 订单支付成功时间，用于计算是否进入派单
     */
    private LocalDateTime paySuccessTime;

    /**
     * 经度
     */
    private Double lon;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 服务数量
     */
    private Integer purNum;

    /**
     * 抢单是否超时
     */
    private Integer isTimeOut;

    /**
     * 抢单列表排序字段
     */
    private Long sortBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
