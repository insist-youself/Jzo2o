package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户端-区域展示响应模型
 *
 * @author itcast
 * @create 2023/8/28 15:54
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("区域展示响应模型")
public class RegionDisplayResDTO {
    /**
     * 区域id
     */
    @ApiModelProperty("区域id")
    private Long id;

    /**
     * 区域编码
     */
    @ApiModelProperty("区域编码")
    private String cityCode;

    /**
     * 区域名称
     */
    @ApiModelProperty("区域名称")
    private String name;

    /**
     * 活动状态，0：草稿，1：禁用，:2：启用
     */
    @ApiModelProperty("活动状态，0：草稿，1：禁用，:2：启用")
    private Integer activeStatus;
}
