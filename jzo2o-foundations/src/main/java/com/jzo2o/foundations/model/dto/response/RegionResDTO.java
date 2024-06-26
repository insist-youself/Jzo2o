package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 区域响应值
 *
 * @author itcast
 * @create 2023/7/4 11:53
 **/
@Data
@ApiModel("区域响应值")
public class RegionResDTO {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 城市编码
     */
    @ApiModelProperty("城市编码")
    private String cityCode;

    /**
     * 区域名称
     */
    @ApiModelProperty("区域名称")
    private String name;

    /**
     * 负责人名称
     */
    @ApiModelProperty("负责人名称")
    private String managerName;

    /**
     * 负责人电话
     */
    @ApiModelProperty("负责人电话")
    private String managerPhone;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    /**
     * 活动状态，0：草稿，1：禁用，:2：启用
     */
    @ApiModelProperty("活动状态，0：草稿，1：禁用，:2：启用")
    private Integer activeStatus;
}
