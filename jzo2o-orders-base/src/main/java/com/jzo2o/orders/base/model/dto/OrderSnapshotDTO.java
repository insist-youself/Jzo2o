package com.jzo2o.orders.base.model.dto;

//import com.jzo2o.statemachine.core.StateMachineSnapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单快照
 *
 * @author itcast
 * @create 2023/8/19 10:30
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSnapshotDTO  {
    /**
     * 订单id
     */
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
     * 订单状态，0：待支付，100：派单中，200：待服务，300：服务中，500：订单完成，600：订单取消，700已关闭
     */
    private Integer ordersStatus;

    /**
     * 支付状态，2：待支付，4：支付成功
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
    private String lon;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 评价时间
     */
    private LocalDateTime evaluationTime;

    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;

    /**
     * 订单更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 支付服务交易单号
     */
    private Long tradingOrderNo;

    /**
     * 支付服务退款单号
     */
    private Long refundNo;



    /**
     * 支付渠道【支付宝、微信、现金、免单挂账】
     */
    private String tradingChannel;


    /**
     * 三方流水,微信支付订单号或支付宝订单号
     */
    private String thirdOrderId;

    /**
     * 退款三方流水,微信支付订单号或支付宝订单号
     */
    private String thirdRefundOrderId;


    /**
     * 取消人id
     */
    private Long cancellerId;

    /**
     * 取消人名称
     */
    private String cancelerName;

    /**
     * 取消人类型
     */
    private Integer cancellerType;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 实际服务完成时间
     */
    private LocalDateTime realServeEndTime;

    /**
     * 评价状态
     */
    private Integer evaluationStatus;


//    @Override
//    public String getSnapshotId() {
//        return String.valueOf(id);
//    }
//
//    @Override
//    public Integer getSnapshotStatus() {
//        return ordersStatus;
//    }
//
//    @Override
//    public void setSnapshotId(String snapshotId) {
//        this.id = Long.parseLong(snapshotId);
//    }
//
//    @Override
//    public void setSnapshotStatus(Integer snapshotStatus) {
//        this.ordersStatus = snapshotStatus;
//    }
}
