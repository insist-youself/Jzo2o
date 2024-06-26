package com.jzo2o.foundations.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 服务项新增更新
 *
 * @author itcast
 * @create 2023/7/3 14:43
 **/
@Data
@ApiModel("服务项新增更新")
public class ServeItemUpsertReqDTO {

    /**
     * 服务类型id
     */
    @ApiModelProperty(value = "服务类型id", required = true)
    private Long serveTypeId;

    /**
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称", required = true)
    private String name;

    /**
     * 服务图标
     */
    @ApiModelProperty(value = "服务图标", required = true)
    private String serveItemIcon;

    /**
     * 服务图片
     */
    @ApiModelProperty(value = "服务图片", required = true)
    private String img;

    /**
     * 服务单位
     */
    @ApiModelProperty(value = "服务单位", required = true)
    private Integer unit;

    /**
     * 服务描述
     */
    @ApiModelProperty(value = "服务描述", required = true)
    private String description;

    /**
     * 服务详图
     */
    @ApiModelProperty(value = "服务详图", required = true)
    private String detailImg;

    /**
     * 参考价格
     */
    @ApiModelProperty(value = "参考价格", required = true)
    private BigDecimal referencePrice;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段", required = true)
    private Integer sortNum;
}
