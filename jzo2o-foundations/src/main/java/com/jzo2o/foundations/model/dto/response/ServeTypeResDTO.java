package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务类型响应值
 *
 * @author itcast
 * @create 2023/7/4 11:53
 **/
@Data
@ApiModel("服务类型响应值")
public class ServeTypeResDTO {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 服务类型编码
     */
    @ApiModelProperty("服务类型编码")
    private String code;

    /**
     * 服务类型名称
     */
    @ApiModelProperty("服务类型名称")
    private String name;

    /**
     * 服务类型图标
     */
    @ApiModelProperty("服务类型图标")
    private String serveTypeIcon;

    /**
     * 服务类型图片
     */
    @ApiModelProperty("服务类型图片")
    private String img;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private Integer sortNum;

    /**
     * 活动状态，0：草稿，1：禁用，:2：启用
     */
    @ApiModelProperty("活动状态，0：草稿，1：禁用，:2：启用")
    private Integer activeStatus;

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
}
