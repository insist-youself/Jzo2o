package com.jzo2o.orders.dispatch.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author itcast
 */
@ApiModel("派单详情")
@Data
public class OrdersDispatchDetailResDTO {

    @ApiModelProperty("服务信息")
    private ServeInfo serveInfo;
    @ApiModelProperty("客户信息")
    private CustomerInfo customerInfo;
    @ApiModelProperty("订单信息")
    private OrdersInfo ordersInfo;


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
        @ApiModelProperty(value = "服务费用", required = true)
        private BigDecimal serveFee;
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
    }

}
