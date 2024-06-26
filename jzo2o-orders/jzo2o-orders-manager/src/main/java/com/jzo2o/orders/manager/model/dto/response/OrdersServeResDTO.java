package com.jzo2o.orders.manager.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author itcast
 */
@Data
@ApiModel("服务分页查询相关模型")
public class OrdersServeResDTO {

    @ApiModelProperty(value = "服务单id",required = true)
    private Long id;

    @ApiModelProperty(value = "服务分类名称",required = true)
    private String serveTypeName;

    @ApiModelProperty("服务名称")
    private String serveItemName;

    @ApiModelProperty(value = "服务地址",required = true)
    private String serveAddress;

    @ApiModelProperty(value = "服务开始时间",required = true)
    private LocalDateTime serveStartTime;

    @ApiModelProperty("服务图片地址")
    private String serveItemImg;

    @ApiModelProperty(value = "订单金额",required = true)
    private BigDecimal ordersAmount;

    @ApiModelProperty(value = "创建时间",required = true)
    private LocalDateTime createTime;

    @ApiModelProperty("服务状态：服务状态，0：待分配，1：待服务，2：服务中，3：服务完成，4：已取消，5：被退单")
    private Integer serveStatus;

    @ApiModelProperty("取消时间")
    private LocalDateTime cancelTime;

    @ApiModelProperty("实际服务开始时间")
    private LocalDateTime realServeStartTime;

    @ApiModelProperty("实际服务结束时间")
    private LocalDateTime realServeEndTime;

    @ApiModelProperty("订单id")
    private Long ordersId;

    @ApiModelProperty("排序字段")
    private Long sortBy;


}
