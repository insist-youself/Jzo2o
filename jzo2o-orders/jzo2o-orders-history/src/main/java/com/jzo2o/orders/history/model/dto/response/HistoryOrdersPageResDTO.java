package com.jzo2o.orders.history.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("历史订单分页列表信息")
@Data
public class HistoryOrdersPageResDTO {
    @ApiModelProperty("订单编号")
    private Long id;
    @ApiModelProperty("服务类型名称")
    private String serveTypeName;
    @ApiModelProperty("服务名称")
    private String serveItemName;
    @ApiModelProperty("下单时间")
    private LocalDateTime placeOrderTime;
    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;
    @ApiModelProperty("实付金额")
    private BigDecimal realPayAmount;
    @ApiModelProperty("预约时间")
    private LocalDateTime serveStartTime;
    @ApiModelProperty("排序时间，对应服务完成时间；服务取消时间；退款时间")
    private LocalDateTime sortTime;
    @ApiModelProperty("服务人姓名")
    private String serveProviderStaffName;
    @ApiModelProperty("服务人手机号")
    private String serveProviderStaffPhone;
    @ApiModelProperty("订单状态,500：订单完成,600：已取消，700：已关闭(即已退款)")
    private Integer ordersStatus;
    @ApiModelProperty("服务地址")
    private String serveAddress;
    @ApiModelProperty("支付状态，2：未支付，4：未支付")
    private Integer payStatus;
    @ApiModelProperty("退款状态，2：退款成功")
    private Integer refundStatus;
}
