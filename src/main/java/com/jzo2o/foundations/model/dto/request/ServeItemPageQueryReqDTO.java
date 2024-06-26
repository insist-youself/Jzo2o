package com.jzo2o.foundations.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务项分页查询类
 *
 * @author itcast
 * @create 2023/7/4 12:43
 **/
@Data
@ApiModel("服务项分页查询类")
public class ServeItemPageQueryReqDTO extends PageQueryDTO {

    @ApiModelProperty("服务项名称")
    private String name;

    @ApiModelProperty("服务类型id")
    private Long serveTypeId;

    @ApiModelProperty("活动状态，0：草稿，1禁用，2启用")
    private Integer activeStatus;
}
