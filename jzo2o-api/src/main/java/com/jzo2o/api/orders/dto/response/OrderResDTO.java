package com.jzo2o.api.orders.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单响应数据
 *
 * @author itcast
 * @create 2023/7/20 21:19
 **/
@Data
@ApiModel("订单响应数据")
public class OrderResDTO {
    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long id;

    /**
     * 订单编码
     */
    @Deprecated
    @ApiModelProperty("订单编码")
    private String ordersCode;

    /**
     * 服务类型id
     */
    @ApiModelProperty("服务类型id")
    private Long serveTypeId;

    /**
     * 服务类型名称
     */
    @ApiModelProperty("服务类型名称")
    private String serveTypeName;

    /**
     * 服务项id
     */
    @ApiModelProperty("服务项id")
    private Long serveItemId;

    /**
     * 服务项名称
     */
    @ApiModelProperty("服务项名称")
    private String serveItemName;

    /**
     * 服务项图片
     */
    @ApiModelProperty("服务项图片")
    private String serveItemImg;

    /**
     * 服务单位
     */
    @ApiModelProperty("服务单位")
    private Integer unit;

    /**
     * 服务id
     */
    @ApiModelProperty("服务id")
    private Long serveId;

    /**
     * 订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：订单取消
     */
    @ApiModelProperty("订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：订单取消")
    private Integer ordersStatus;

    @ApiModelProperty("支付状态，0：待支付，1：支付成功")
    private Integer payStatus;

    @ApiModelProperty("退款状态，0：发起退款，1：退款中，2：退款成功  3：退款失败")
    private Integer refundStatus;
    /**
     * 评价状态，0：待评价，1：已评价
     */
    @ApiModelProperty("评价状态，0：待评价，1：已评价")
    private Integer evaluationStatus;
    /**
     * 单价
     */
    @ApiModelProperty("单价")
    private BigDecimal price;

    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    private Integer purNum;

    /**
     * 实际支付金额
     */
    @ApiModelProperty("实际支付金额")
    private BigDecimal realPayAmount;

    /**
     * 服务详细地址
     */
    @ApiModelProperty("服务详细地址")
    private String serveAddress;

    /**
     * 联系人手机号
     */
    @ApiModelProperty("联系人手机号")
    private String contactsPhone;

    /**
     * 联系人姓名
     */
    @ApiModelProperty("联系人姓名")
    private String contactsName;

    /**
     * 服务人id，机构接单则该id为机构下属服务人员
     */
    @ApiModelProperty("服务人id，机构接单则该id为机构下属服务人员")
    private Long serverId;

    /**
     * 服务人姓名
     */
    @ApiModelProperty("服务人姓名")
    private String serverName;

    /**
     * 支付超时时间，该时间只对待支付有意义
     */
    @ApiModelProperty("支付超时时间，该时间只对待支付有意义")
    private LocalDateTime paymentTimeout;

    /**
     * 服务开始时间
     */
    @ApiModelProperty("服务开始时间")
    private LocalDateTime serveStartTime;

    /**
     * 服务结束时间
     */
    @ApiModelProperty("服务结束时间")
    private LocalDateTime serveEndTime;

    /**
     * 服务实际开始时间
     */
    @ApiModelProperty("服务实际开始时间")
    private LocalDateTime serveActualStartTime;

    /**
     * 服务实际结束时间
     */
    @ApiModelProperty("服务实际结束时间")
    private LocalDateTime serveActualEndTime;

    /**
     * 取消/关闭原因
     */
    @ApiModelProperty("取消/关闭原因")
    private String cancelReason;

    /**
     * 取消时间
     */
    @ApiModelProperty("取消/退款时间")
    private LocalDateTime cancelTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
