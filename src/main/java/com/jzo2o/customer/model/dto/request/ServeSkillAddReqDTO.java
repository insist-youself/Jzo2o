package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 服务技能新增请求
 *
 * @author itcast
 * @create 2023/7/18 14:27
 **/
@Data
@ApiModel("服务技能新增请求")
public class ServeSkillAddReqDTO {

    /**
     * 服务类型id
     */
    @NotNull(message = "服务类型id不能为空")
    @ApiModelProperty(value = "服务类型id", required = true)
    private Long serveTypeId;

    /**
     * 服务类型名称
     */
    @ApiModelProperty(value = "服务类型名称", required = true)
    private String serveTypeName;

    /**
     * 服务项id
     */
    @NotNull(message = "服务项id不能为空")
    @ApiModelProperty(value = "服务项id", required = true)
    private Long serveItemId;

    /**
     * 服务项名称
     */
    @ApiModelProperty(value = "服务项名称", required = true)
    private String serveItemName;
}
