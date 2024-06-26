package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务简略响应信息
 *
 * @author itcast
 * @create 2023/7/7 14:50
 **/
@Data
@ApiModel("服务简略响应信息")
public class ServeSimpleResDTO {

    /**
     * 服务id
     */
    @ApiModelProperty("服务id")
    private Long id;

    /**
     * 服务项id
     */
    @ApiModelProperty("服务项id")
    private Long serveItemId;

    /**
     * 服务项名称
     */
    @ApiModelProperty("服务项名称")
    private String serveItemName;

    /**
     * 服务项图标
     */
    @ApiModelProperty("服务项图标")
    private String serveItemIcon;

    /**
     * 服务项排序字段
     */
    @ApiModelProperty("服务项排序字段")
    private Integer serveItemSortNum;
}
