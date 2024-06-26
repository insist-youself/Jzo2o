package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务技能项响应结果
 *
 * @author itcast
 * @create 2023/7/18 19:16
 **/
@Data
@ApiModel("服务技能项响应结果")
public class ServeSkillItemResDTO {
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
     * 是否被选中
     */
    @ApiModelProperty("是否被选中")
    private Boolean isSelected;
}
