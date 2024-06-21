package com.jzo2o.orders.history.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("历史订单列表模型")
@Data
public class HistoryOrdersListResDTO {
    @ApiModelProperty("订单编号")
    private Long id;
    @ApiModelProperty("服务类型名称")
    private String serveTypeName;
    @ApiModelProperty("服务名称")
    private String serveItemName;
    @ApiModelProperty("订单金额，即实付金额")
    private BigDecimal orderAmount;
    @ApiModelProperty("预约时间")
    private LocalDateTime serveStartTime;
    @ApiModelProperty("排序时间，对应服务完成时间；服务取消时间；退款时间")
    private LocalDateTime sortTime;
    @ApiModelProperty("订单状态,500：订单完成,600：已取消，700：已关闭(即已退款)")
    private Integer ordersStatus;
    @ApiModelProperty("服务图片")
    private String serveItemImg;
    @ApiModelProperty("交易状态，2：未付款，4：退款成功")
    private Integer payStatus;
    @ApiModelProperty("服务地址")
    private String serveAddress;

}
