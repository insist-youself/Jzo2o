package com.jzo2o.api.foundations.dto.response;

import com.jzo2o.api.foundations.dto.ServeItemSimpleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 服务技能目录响应结果
 *
 * @author itcast
 * @create 2023/7/18 19:16
 **/
@Data
@ApiModel("服务技能目录响应结果")
public class ServeTypeCategoryResDTO {
    /**
     * 服务类型d
     */
    @ApiModelProperty("服务类型id")
    private Long serveTypeId;

    /**
     * 服务类型名称
     */
    @ApiModelProperty("服务类型名称")
    private String serveTypeName;

    /**
     * 服务项列表
     */
    @ApiModelProperty("服务项列表")
    private List<ServeItemSimpleDTO> serveItemList;
}
