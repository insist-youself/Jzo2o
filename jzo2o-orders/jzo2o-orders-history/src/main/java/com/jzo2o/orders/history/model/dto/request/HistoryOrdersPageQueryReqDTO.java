package com.jzo2o.orders.history.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ApiModel("历史订单分页查询模型")
@Data
public class HistoryOrdersPageQueryReqDTO extends PageQueryDTO {
    @ApiModelProperty(value = "订单编号")
    private Long id;
    @ApiModelProperty(value = "时间查询下限")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime minSortTime;
    @ApiModelProperty(value = "时间查询上限")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime maxSortTime;
    @ApiModelProperty("订单状态,500：订单完成,600：已取消，700：已关闭(即已退款)")
    private Integer ordersStatus;
    @ApiModelProperty("服务人员手机号")
    private String serveProviderStaffPhone;
    @ApiModelProperty("支付状态,2:未支付，4：已支付")
    private Integer payStatus;
    @ApiModelProperty("退款状态，2:退款成功，3：退款失败")
    private Integer refundStatus;

}
