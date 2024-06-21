package com.jzo2o.orders.history.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ApiModel("历史订单分页查询模型")
@Data
public class HistoryOrdersListQueryReqDTO  {
    @ApiModelProperty(value = "时间查询下限")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime minSortTime;
    @ApiModelProperty(value = "时间查询上限")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime maxSortTime;
    @ApiModelProperty(value = "上次查询数据最后一条数据的sortTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSortTime;


}
