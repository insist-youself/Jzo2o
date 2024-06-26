package com.jzo2o.orders.history.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.jzo2o.common.utils.JsonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author itcast
 * @since 2023-09-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "history_orders",autoResultMap = true)
public class HistoryOrders implements Serializable {

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
     * 服务人
     */
    private Long serveProviderId;

    /**
     * 服务人类型，2：服务人员，3：机构
     */
    private Integer serveProviderType;

    /**
     * 服务项id
     */
    private Long serveItemId;

    /**
     * 服务id
     */
    private Long serveId;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 服务类型名称
     */
    private String serveTypeName;

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
     * 订单状态，500：订单完成，600：已取消，700：已关闭
     */
    private Integer ordersStatus;

    /**
     * 支付状态，2：未支付，4：支付完成
     */
    private Integer payStatus;

    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 订单完成时间
     */
    private LocalDateTime tradeFinishTime;

    /**
     * 支付渠道
     */
    private String tradingChannel;

    /**
     * 派单时间
     */
    private LocalDateTime dispatchTime;

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
     * 取消人
     */
    private String cancelerName;

    /**
     * 服务人姓名
     */
    private String serveProviderStaffName;

    /**
     * 服务人手机号
     */
    private String serveProviderStaffPhone;

    /**
     * 机构名称
     */
    private String institutionName;

    /**
     * 机构电话
     */
    private String institutionPhone;

    /**
     * 下单时间
     */
    private LocalDateTime placeOrderTime;

    /**
     * 服务开始时间
     */
    private LocalDateTime serveStartTime;

    /**
     * 实际服务开始时间
     */
    private LocalDateTime realServeStartTime;

    /**
     * 实际服务结束时间
     */
    private LocalDateTime realServeEndTime;

    /**
     * 服务开始图片
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> serveBeforeImgs;

    /**
     * 服务开始说明
     */
    private String serveBeforeIllustrate;

    /**
     * 服务完成图片
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> serveAfterImgs;

    /**
     * 服务完成说明
     */
    private String serveAfterIllustrate;

    /**
     * 支付超时时间，该时间只对待支付有意义
     */
    private LocalDateTime paymentTimeout;

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
     * 取消/被退单时间
     */
    private LocalDateTime cancelTime;

    /**
     * 取消/被退单原因
     */
    private String cancelReason;

    /**
     * 评价时间
     */
    private LocalDateTime evaluationTime;

    /**
     * 评分
     */
    private Double evaluationScore;

    /**
     * 用户端是否展示，1：展示，0：隐藏
     */
    private Integer display;

    /**
     * 排序时间字段
     */
    private LocalDateTime sortTime;


}
