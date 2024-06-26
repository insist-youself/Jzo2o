package com.jzo2o.foundations.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 服务类型新增更新
 *
 * @author itcast
 * @create 2023/7/3 14:43
 **/
@Data
@ApiModel("服务类型新增更新")
public class ServeTypeUpsertReqDTO {
    /**
     * 服务类型名称
     */
    @ApiModelProperty(value = "服务类型名称", required = true)
    private String name;

    /**
     * 服务类型图标
     */
    @ApiModelProperty(value = "服务类型图标", required = true)
    private String serveTypeIcon;

    /**
     * 服务类型图片
     */
    @ApiModelProperty(value = "服务类型图片", required = true)
    private String img;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段", required = true)
    private Integer sortNum;
}
