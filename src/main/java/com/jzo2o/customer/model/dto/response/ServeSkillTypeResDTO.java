package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务技能类型响应
 *
 * @author itcast
 * @create 2023/7/18 17:39
 **/
@Data
@ApiModel("服务技能类型响应")
public class ServeSkillTypeResDTO {
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
     * 服务类型下属技能数量
     */
    @ApiModelProperty("服务类型下属技能数量")
    private Integer count;
}
