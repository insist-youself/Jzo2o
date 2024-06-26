package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 区域简略响应值
 *
 * @author itcast
 * @create 2023/7/4 11:53
 **/
@Data
@ApiModel("区域简略响应值")
public class RegionSimpleResDTO {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 区域名称
     */
    @ApiModelProperty("区域名称")
    private String name;

    /**
     * 城市编码
     */
    @ApiModelProperty("城市编码")
    private String cityCode;
}
