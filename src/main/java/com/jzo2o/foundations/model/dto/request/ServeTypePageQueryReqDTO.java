package com.jzo2o.foundations.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 服务类型分页查询类
 *
 * @author itcast
 * @create 2023/7/4 12:43
 **/
@Data
@ApiModel("服务类型分页查询类")
public class ServeTypePageQueryReqDTO extends PageQueryDTO {
}
