package com.jzo2o.orders.base.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 派单池
 * </p>
 *
 * @author itcast
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders_dispatch")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersDispatch implements Serializable {

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
     * 订单金额
     */
    private BigDecimal ordersAmount;

    /**
     * 服务开始时间
     */
    private LocalDateTime serveStartTime;

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
     * 是否转人工
     */
    private Integer isTransferManual;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
