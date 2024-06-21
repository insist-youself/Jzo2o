package com.jzo2o.orders.history.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ApiModel("历史服务单分页查询模型")
@Data
public class HistoryOrdersServePageQueryReqDTO extends PageQueryDTO {
    @ApiModelProperty(value = "订单编号")
    private Long id;
    @ApiModelProperty(value = "时间查询下限")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime minSortTime;
    @ApiModelProperty(value = "时间查询上限")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime maxSortTime;
    @ApiModelProperty("订单状态,3:服务完成，4：已关闭")
    private Integer serveStatus;
}
