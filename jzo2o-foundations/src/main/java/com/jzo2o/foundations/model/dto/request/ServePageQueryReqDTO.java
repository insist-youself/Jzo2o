package com.jzo2o.foundations.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务分页查询类
 *
 * @author itcast
 * @create 2023/7/4 12:43
 **/
@Data
@ApiModel("服务分页查询类")
public class ServePageQueryReqDTO extends PageQueryDTO {
    @ApiModelProperty(value = "区域id", required = true)
    private Long regionId;
}
