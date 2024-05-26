package com.jzo2o.orders.manager.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author itcast
 */
@Data
@ApiModel("服务单分页查询相关模型")
public class OrdersServePageQueryReqDTO extends PageQueryDTO {

    @ApiModelProperty(value = "服务状态，0：待分配，1：待上门，2：服务中（待完工），3：服务完成,4:取消，5：被退单")
    private Integer serveStatus;

    @ApiModelProperty(value = "服务单编码")
    private Long id;

    @ApiModelProperty(value = "服务项id")
    private String serveItemId;

    @ApiModelProperty(value = "服务时间 - 最小")
    private Date minServeStartTime;

    @ApiModelProperty(value = "服务时间 - 最大")
    private Date maxServeStartTime;
}
