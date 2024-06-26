package com.jzo2o.customer.model.dto.response;

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
public class ServeSkillCategoryResDTO {
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
     * 下属服务技能数量
     */
    @ApiModelProperty("下属服务技能数量")
    private Integer count;

    /**
     * 服务技能项列表
     */
    @ApiModelProperty("服务技能项列表")
    private List<ServeSkillItemResDTO> serveSkillItemResDTOList;
}
