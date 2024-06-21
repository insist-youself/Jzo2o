package com.jzo2o.orders.history.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("历史服务单列表信息")
@Data
public class HistoryOrdersServeResDTO {
    @ApiModelProperty("订单编号")
    private Long id;
    @ApiModelProperty("服务类型名称")
    private String serveTypeName;
    @ApiModelProperty("服务名称")
    private String serveItemName;
    @ApiModelProperty("订单金额，即实付金额")
    private BigDecimal ordersAmount;
    @ApiModelProperty("预约时间")
    private LocalDateTime serveStartTime;
    @ApiModelProperty("排序时间")
    private LocalDateTime sortTime;
    @ApiModelProperty("服务端订单状态,3：服务完成，4：已取消")
    private Integer serveStatus;
    @ApiModelProperty("服务图片")
    private String serveItemImg;
    @ApiModelProperty("实际服务开始时间")
    private LocalDateTime realServeStartTime;
    @ApiModelProperty("实际服务结束时间")
    private LocalDateTime realServeEndTime;
    @ApiModelProperty("服务地址")
    private String serveAddress;
}
