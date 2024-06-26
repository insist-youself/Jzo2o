package com.jzo2o.orders.manager.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理端订单详情
 *
 * @author itcast
 * @create 2023/9/12 21:05
 **/
@Data
@ApiModel("管理端订单详情响应数据")
public class OperationOrdersDetailResDTO {
    @ApiModelProperty("状态流转")
    private List<OrderProgress> ordersProgress;

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
        @ApiModelProperty("订单id")
        private Long id;

        @ApiModelProperty("订单状态，0：待支付，100：派单中，200：待服务，300：服务中，500：订单完成，600：订单取消，700已关闭")
        private Integer ordersStatus;

        @ApiModelProperty("服务项名称")
        private String serveItemName;

        @ApiModelProperty("下单时间")
        private LocalDateTime createTime;

        @ApiModelProperty("客户名称")
        private String contactsName;

        @ApiModelProperty("客户电话")
        private String contactsPhone;

        @ApiModelProperty("服务地址")
        private String serveAddress;

        @ApiModelProperty("预约时间")
        private LocalDateTime serveStartTime;

        @ApiModelProperty("订单总金额")
        private BigDecimal totalAmount;

        @ApiModelProperty("实际支付金额")
        private BigDecimal realPayAmount;

        @ApiModelProperty("购买数量")
        private Integer purNum;

        @ApiModelProperty("服务单位")
        private Integer unit;

        @ApiModelProperty("服务人员类型，2：服务人员，3：机构")
        private Integer serveProviderType;

        @ApiModelProperty("服务人员/机构名称")
        private String serveProviderName;

        @ApiModelProperty("服务人员/机构电话")
        private String serveProviderPhone;

        @ApiModelProperty("机构下属服务人员姓名")
        private String institutionStaffName;

        @ApiModelProperty("机构下属服务人员电话")
        private String institutionStaffPhone;

    }

    @Data
    @ApiModel("支付记录模型")
    public static class PayInfo {
        @ApiModelProperty("支付状态，2：待支付，4：支付成功")
        private Integer payStatus;

        @ApiModelProperty("支付渠道，ALI_PAY：支付宝，WECHAT_PAY：微信")
        private String tradingChannel;

        @ApiModelProperty("三方流水,微信支付订单号或支付宝订单号")
        private String thirdOrderId;

        @ApiModelProperty("订单总金额")
        private BigDecimal totalAmount;

        @ApiModelProperty("实付金额")
        private BigDecimal realPayAmount;
    }

    @Data
    @ApiModel("服务信息模型")
    public static class ServeInfo {
        @ApiModelProperty("服务人员类型")
        private Integer serveProviderType;

        @ApiModelProperty("服务人员/机构名称")
        private String serveProviderName;

        @ApiModelProperty("机构下属服务人员姓名")
        private String institutionStaffName;

        @ApiModelProperty("服务实际开始时间")
        private LocalDateTime realServeStartTime;

        @ApiModelProperty("服务开始图片")
        private List<String> serveBeforeImgs;

        @ApiModelProperty("服务开始说明")
        private String serveBeforeIllustrate;

        @ApiModelProperty("服务完成时间")
        private LocalDateTime realServeEndTime;

        @ApiModelProperty("服务完成图片")
        private List<String> serveAfterImgs;

        @ApiModelProperty("服务完成说明")
        private String serveAfterIllustrate;
    }

    @Data
    @ApiModel("退款信息模型")
    public static class RefundInfo {
        @ApiModelProperty("退款状态：0:发起退款，1:退款中，2：成功，3：失败")
        private Integer refundStatus;

        @ApiModelProperty("取消人类型，0：系统，1：普通用户，4：管理员")
        private Integer cancellerType;

        @ApiModelProperty("退款人,退款人和取消人保持一致")
        private String cancelerName;

        @ApiModelProperty("退款理由和取消理由一致")
        private String cancelReason;

        @ApiModelProperty("支付渠道，ALI_PAY：支付宝，WECHAT_PAY：微信")
        private String tradingChannel;

        @ApiModelProperty("退款三方流水,微信支付订单号或支付宝订单号")
        private String thirdRefundOrderId;

        @ApiModelProperty("退款金额，和实付金额保持一致")
        private BigDecimal realPayAmount;

        @ApiModelProperty("退款时间;和取消时间保持一致")
        private LocalDateTime cancelTime;
    }

    @Data
    @ApiModel("订单取消模型")
    public static class CancelInfo {
        @ApiModelProperty("取消人类型，0：系统，1：普通用户，4：管理员")
        private Integer cancellerType;

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
    public static class OrderProgress {
        @ApiModelProperty("订单状态，0：待支付，100：派单中，200：待服务，300：服务中，500：订单完成，600：订单取消，700已退单")
        private Integer status;

        @ApiModelProperty("时间")
        private LocalDateTime dateTime;
    }
}
