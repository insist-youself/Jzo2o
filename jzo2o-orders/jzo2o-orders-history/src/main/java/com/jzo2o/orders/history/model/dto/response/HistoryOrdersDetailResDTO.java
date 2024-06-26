package com.jzo2o.orders.history.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author itcast
 */
@Data
@ApiModel("历史订单详情信息")
public class HistoryOrdersDetailResDTO {

    @ApiModelProperty("状态流转")
    private List<OrdersProgress> ordersProgresses;

    @ApiModelProperty("订单信息")
    private OrderInfo orderInfo;

    @ApiModelProperty("支付信息")
    private PayInfo payInfo;

    @ApiModelProperty("服务信息")
    private ServeInfo serveInfo;

    @ApiModelProperty("退款信息")
    private RefundInfo refundInfo;

    @ApiModelProperty("订单取消理由")
    private CancelInfo cancelInfo;

    @Data
    @ApiModel("订单信息模型")
    public static class OrderInfo {
        @ApiModelProperty("服务单id")
        private Long id;
        @ApiModelProperty(value = "服务单状态,0：待分配，1：待服务，2：服务中，3：服务完成，4：已取消，5：被退单", required = true)
        private Integer ordersStatus;
        @ApiModelProperty("下单时间")
        private LocalDateTime placeOrderTime;
        @ApiModelProperty("服务类型名称")
        private String serveTypeName;
        @ApiModelProperty("服务项名称")
        private String serveItemName;
        @ApiModelProperty("服务图片地址")
        private String serveItemImg;
        @ApiModelProperty("客户电话")
        private String contactsPhone;
        @ApiModelProperty("客户名称")
        private String contactsName;
        @ApiModelProperty("预约时间")
        private LocalDateTime serveStartTime;
        @ApiModelProperty("服务地址")
        private String serveAddress;
        @ApiModelProperty("服务人员姓名")
        private String serveProviderStaffName;
        @ApiModelProperty("服务人员电话")
        private String serveProviderStaffPhone;
        @ApiModelProperty("服务人员类型")
        private Integer serveProviderType;
        @ApiModelProperty("购买数量")
        private Integer purNum;
        @ApiModelProperty("服务单位")
        private Integer unit;
        @ApiModelProperty("机构名称")
        private String institutionName;
        @ApiModelProperty("机构电话")
        private String institutionPhone;
        @ApiModelProperty("支付状态，0：待支付，1：支付成功，2：已关闭，3：退款成功")
        private Integer payStatus;
        @ApiModelProperty("单价")
        private BigDecimal price;
        @ApiModelProperty("实际支付金额")
        private BigDecimal realPayAmount;

    }

    @ApiModel("支付记录模型")
    @Data
    public static class PayInfo {
        @ApiModelProperty("支付状态，1：支付成功，2：已关闭")
        private Integer payStatus;
        @ApiModelProperty("支付渠道，ALI_PAY：支付宝，WECHAT_PAY：微信")
        private String tradingChannel = "WECHAT_PAY";
        @ApiModelProperty("三方流水,微信支付订单号或支付宝订单号")
        private String thirdOrderId = UUID.randomUUID().toString();
        @ApiModelProperty("订单总金额")
        private BigDecimal totalAmount;
        @ApiModelProperty("实付金额")
        private BigDecimal realPayAmount;
    }

    @ApiModel("服务信息模型")
    @Data
    public static class ServeInfo {
        @ApiModelProperty("服务实际开始时间")
        private LocalDateTime realServeStartTime;
        @ApiModelProperty("服务开始图片")
        private List<String> serveBeforeImgs;
        @ApiModelProperty("服务开始说明")
        private String serveBeforeIllustrate;

        @ApiModelProperty("服务完成图片")
        private List<String> serveAfterImgs;

        @ApiModelProperty("服务完成说明")
        private String serveAfterIllustrate;

        @ApiModelProperty("服务完成时间")
        private LocalDateTime realServeEndTime;

        @ApiModelProperty("服务人姓名")
        private String serverName;
    }

    @ApiModel("退款信息模型")
    @Data
    public static class RefundInfo {
        @ApiModelProperty("退款状态：1:退款成功")
        private Integer refundStatus;
        @ApiModelProperty("退款人,退款人和取消人保持一致")
        private String cancelerName;
        @ApiModelProperty("退款理由和取消理由一致")
        private String cancelReason;
        @ApiModelProperty("支付渠道，ALI_PAY：支付宝，WECHAT_PAY：微信")
        private String tradingChannel = "WECHAT_PAY";
        @ApiModelProperty("退款三方流水,微信支付订单号或支付宝订单号")
        private String thirdRefundOrderId;
        @ApiModelProperty("退款金额，和实付金额保持一致")
        private BigDecimal realPayAmount;
        @ApiModelProperty("退款时间;和取消时间保持一致")
        private LocalDateTime cancelTime;
    }

    @ApiModel("订单取消模型")
    @Data
    public static class CancelInfo {
        @ApiModelProperty("取消人")
        private String cancelerName;
        @ApiModelProperty("取消时间")
        private LocalDateTime cancelTime;
        @ApiModelProperty("取消理由")
        private String cancelReason;

    }

    @Data
    @ApiModel("状态节点流转模型")
    @AllArgsConstructor
    public static class OrdersProgress {
        @ApiModelProperty("订单状态，100：已支付，200：派单完成500：订单完成，600：已取消or已退单")
        private Integer status;
        @ApiModelProperty("时间")
        private LocalDateTime dateTime;
    }



}
