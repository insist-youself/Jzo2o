package com.jzo2o.orders.manager.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author itcast
 */
@Data
@ApiModel("订单详情信息")
public class OrdersServeDetailResDTO {
    @ApiModelProperty("服务单id")
    private Long id;
    @ApiModelProperty(value = "服务单状态,0：待分配，1：待服务，2：服务中，3：服务完成，4：已取消，5：被退单", required = true)
    private Integer serveStatus;

    @ApiModelProperty("服务信息")
    private ServeInfo serveInfo;

    @ApiModelProperty("客户信息")
    private CustomerInfo customerInfo;

    @ApiModelProperty("订单信息")
    private OrdersInfo ordersInfo;

    @ApiModelProperty("退款信息")
    private RefundInfo refundInfo;

    @ApiModelProperty("取消信息")
    private CancelInfo cancelInfo;
//
//
    @Data
    @ApiModel("取消详情")
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CancelInfo {
        @ApiModelProperty("取消原因")
        private String cancelReason;
        @ApiModelProperty("取消时间")
        private LocalDateTime cancelTime;
    }
    @Data
    @ApiModel("退款信息")
    private static class RefundInfo {
        @ApiModelProperty("退款时间")
        private LocalDateTime refundTime;
        @ApiModelProperty("退款原因")
        private String refundReason;
        @ApiModelProperty("退款金额")
        private BigDecimal refundAmount;
    }
    @Data
    @ApiModel("服务信息")
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ServeInfo {
        @ApiModelProperty(value = "服务类型", required = true)
        private String serveTypeName;
        @ApiModelProperty(value = "服务项名称", required = true)
        private String serveItemName;
        @ApiModelProperty(value = "服务数量", required = true)
        private Integer serveNum;
        @ApiModelProperty(value = "服务单位", required = true)
        private Integer unit;
        @ApiModelProperty(value = "机构服务人", required = true)
        private String institutionStaffName;
        @ApiModelProperty(value = "服务前照片列表")
        private List<String> serveBeforeImgs;
        @ApiModelProperty(value = "服务前照片列表")
        private List<String> serveAfterImgs;
        @ApiModelProperty(value = "服务开始说明")
        private String serveBeforeIllustrate;
        @ApiModelProperty(value = "服务完成说明")
        private String serveAfterIllustrate;
        @ApiModelProperty("服务实际开始时间")
        private LocalDateTime realServeStartTime;
        @ApiModelProperty("服务实际完成时间")
        private LocalDateTime realServeEndTime;
        @ApiModelProperty("服务图片地址")
        private String serveItemImg;
    }


    @Data
    @ApiModel("客户信息")
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomerInfo {
        @ApiModelProperty(value = "联系人", required = true)
        private String contactsName;
        @ApiModelProperty(value = "联系人手机号", required = true)
        private String contactsPhone;
        @ApiModelProperty(value = "服务地址", required = true)
        private String serveAddress;
    }


    @Data
    @ApiModel("订单信息")
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrdersInfo {

        @ApiModelProperty(value = "服务单单编号", required = true)
        private Long ordersId;
        @ApiModelProperty(value = "服务开始时间", required = true)
        private LocalDateTime serveStartTime;
        @ApiModelProperty("订单金额")
        private BigDecimal ordersAmount;
    }

}
