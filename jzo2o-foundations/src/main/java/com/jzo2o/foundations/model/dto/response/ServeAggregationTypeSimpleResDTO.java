package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务分类响应信息
 *
 * @author itcast
 * @create 2023/7/7 14:50
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("服务分类响应信息")
public class ServeAggregationTypeSimpleResDTO {

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
     * 服务类型图片
     */
    @ApiModelProperty("服务类型图片")
    private String serveTypeImg;

    /**
     * 服务类型排序字段
     */
    @ApiModelProperty("服务类型排序字段")
    private Integer serveTypeSortNum;
}
