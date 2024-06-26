package com.jzo2o.foundations.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 区域新增更新
 *
 * @author itcast
 * @create 2023/7/3 14:43
 **/
@Data
@ApiModel("区域新增更新")
public class RegionUpsertReqDTO {

    /**
     * 城市编码
     */
    @ApiModelProperty(value = "城市编码", required = true)
    private String cityCode;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称", required = true)
    private String name;

    /**
     * 负责人名称
     */
    @ApiModelProperty(value = "负责人名称", required = true)
    private String managerName;

    /**
     * 负责人电话
     */
    @ApiModelProperty(value = "负责人电话", required = true)
    private String managerPhone;
}
