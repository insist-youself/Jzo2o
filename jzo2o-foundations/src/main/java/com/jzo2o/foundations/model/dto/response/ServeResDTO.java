package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务响应值
 *
 * @author itcast
 * @create 2023/7/4 11:53
 **/
@Data
@ApiModel("服务响应值")
public class ServeResDTO {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 售卖状态，0：草稿，1下架，2上架
     */
    @ApiModelProperty("售卖状态，0：草稿，1下架，2上架")
    private Integer saleStatus;

    /**
     * 服务id
     */
    @ApiModelProperty("服务项id")
    private Long serveItemId;

    /**
     * 服务名称
     */
    @ApiModelProperty("服务项名称")
    private String serveItemName;

    /**
     * 服务类型id
     */
    @ApiModelProperty("服务类型id")
    private Long serveTypeId;

    /**
     * 服务名称
     */
    @ApiModelProperty("服务类型名称")
    private String serveTypeName;

    /**
     * 区域id
     */
    @ApiModelProperty("区域id")
    private Long regionId;

    /**
     * 参考价格
     */
    @ApiModelProperty("参考价格")
    private BigDecimal referencePrice;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 是否为热门，0非热门，1热门
     */
    @ApiModelProperty("是否为热门，0非热门，1热门")
    private Integer isHot;

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
