package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 首页服务图标
 *
 * @author itcast
 * @create 2023/7/7 14:50
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("首页服务图标")
public class ServeCategoryResDTO {

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
     * 服务类型图标
     */
    @ApiModelProperty("服务类型图标")
    private String serveTypeIcon;
    private String cityCode;

    /**
     * 服务类型排序字段
     */
    @ApiModelProperty("服务类型排序字段")
    private Integer serveTypeSortNum;

    /**
     * 服务项图标列表
     */
    @ApiModelProperty("服务项图标列表")
    private List<ServeSimpleResDTO> serveResDTOList;
}
