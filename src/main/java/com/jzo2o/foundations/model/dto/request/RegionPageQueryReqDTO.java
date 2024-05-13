package com.jzo2o.foundations.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 区域分页查询类
 *
 * @author itcast
 * @create 2023/7/4 12:43
 **/
@Data
@ApiModel("区域分页查询类")
public class RegionPageQueryReqDTO extends PageQueryDTO {
}
