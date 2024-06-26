package com.jzo2o.api.foundations.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务项响应值
 *
 * @author itcast
 * @create 2023/7/4 11:53
 **/
@Data
@ApiModel("服务项响应值")
public class ServeItemResDTO {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 服务编码
     */
    @ApiModelProperty("服务编码")
    private String code;

    /**
     * 服务类型id
     */
    @ApiModelProperty("服务类型id")
    private Long serveTypeId;

    /**
     * 服务类型名称
     */
    @ApiModelProperty("服务类型名称")
    private String serveTypeName;

    /**
     * 服务名称
     */
    @ApiModelProperty("服务名称")
    private String name;

    /**
     * 服务图标
     */
    @ApiModelProperty("服务图标")
    private String serveItemIcon;

    /**
     * 服务图片
     */
    @ApiModelProperty("服务图片")
    private String img;

    /**
     * 服务单位
     */
    @ApiModelProperty("服务单位")
    private Integer unit;

    /**
     * 服务描述
     */
    @ApiModelProperty("服务描述")
    private String description;

    /**
     * 服务详图
     */
    @ApiModelProperty("服务详图")
    private String detailImg;

    /**
     * 参考价格
     */
    @ApiModelProperty("参考价格")
    private BigDecimal referencePrice;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private Integer sortNum;

    /**
     * 活动状态，0：草稿，1禁用，2启用
     */
    @ApiModelProperty("活动状态，0：草稿，1禁用，2启用")
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
