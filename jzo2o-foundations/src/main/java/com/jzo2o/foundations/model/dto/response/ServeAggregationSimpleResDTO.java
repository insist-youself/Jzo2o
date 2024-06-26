package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 服务响应值
 *
 * @author itcast
 * @create 2023/7/4 11:53
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServeAggregationSimpleResDTO {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 服务id
     */
    @ApiModelProperty("服务项id")
    private Long serveItemId;

    /**
     * 服务项名称
     */
    @ApiModelProperty("服务项名称")
    private String serveItemName;

    /**
     * 服务项图片
     */
    @ApiModelProperty("服务项图片")
    private String serveItemImg;

    /**
     * 服务单位
     */
    @ApiModelProperty("服务单位")
    private Integer unit;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 服务详图
     */
    @ApiModelProperty("服务详图")
    private String detailImg;

    /**
     * 城市编码
     */
    @ApiModelProperty("城市编码")
    private String cityCode;
}
