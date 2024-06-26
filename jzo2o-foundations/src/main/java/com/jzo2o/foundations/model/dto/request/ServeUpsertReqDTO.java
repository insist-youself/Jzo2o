package com.jzo2o.foundations.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 服务新增更新
 *
 * @author itcast
 * @create 2023/7/3 14:43
 **/
@Data
@ApiModel("服务新增更新")
public class ServeUpsertReqDTO {

    /**
     * 服务id
     */
    @ApiModelProperty(value = "服务id", required = true)
    private Long serveItemId;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id", required = true)
    private Long regionId;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格", required = true)
    private BigDecimal price;
}
