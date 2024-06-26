package com.jzo2o.orders.base.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 订单所属人
     */
    private Long userId;

    /**
     * 服务类型id
     */
    private Long serveTypeId;

    /**
     * 服务类型名称
     */
    private String serveTypeName;

    /**
     * 服务项id
     */
    private Long serveItemId;

    /**
     * 服务项名称
     */
    private String serveItemName;

    /**
     * 服务项图片
     */
    private String serveItemImg;

    /**
     * 服务单位
     */
    private Integer unit;

    /**
     * 服务id
     */
    private Long serveId;

    /**
     * 订单状态，0：待支付，100：派单中，200：待服务，300：服务中，500：订单完成，600：已取消，700：已关闭
     */
    private Integer ordersStatus;

    /**
     * 交易状态，0：待支付，1：支付成功
     */
    private Integer payStatus;

    /**
     * 退款，0：发起退款，1：退款中，2：退款成功  3：退款失败
     */
    private Integer refundStatus;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 购买数量
     */
    private Integer purNum;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal realPayAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 服务详细地址
     */
    private String serveAddress;

    /**
     * 联系人手机号
     */
    private String contactsPhone;

    /**
     * 联系人姓名
     */
    private String contactsName;

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
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 评价时间
     */
    private LocalDateTime evaluationTime;
    /**
     * 评价状态
     */
    private Integer evaluationStatus;

    /**
     * 用户端是否展示，1：展示，0：隐藏
     */
    private Integer display;

    /**
     * 实际服务完成时间
     */
    private LocalDateTime realServeEndTime;

    /**
     * 排序字段（serve_start_time秒级时间戳+订单id后6位）
     */
    private Long sortBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 支付服务交易单号
     */
    private Long tradingOrderNo;

    /**
     * 第三方支付的交易号
     */
    private String transactionId;

    /**
     * 支付服务退款单号
     */
    private Long refundNo;

    /**
     * 第三方支付的退款单号
     */
    private String refundId;

    /**
     * 支付渠道
     */
    private String tradingChannel;


}
