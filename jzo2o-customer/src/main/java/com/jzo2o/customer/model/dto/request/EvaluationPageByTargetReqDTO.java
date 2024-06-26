package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据对象属性分页查询评价请求
 *
 * @author itcast
 * @create 2023/9/11 21:19
 **/
@Data
@ApiModel("根据对象属性分页查询评价请求")
public class EvaluationPageByTargetReqDTO {

    @ApiModelProperty(value = "评价类型id，6：服务评价，7：师傅评价",required = true)
    private String targetTypeId;

    @ApiModelProperty("评价对象id，服务评价 - 服务项id，师傅评价 - 服务人员/机构id")
    private String targetId;

    @ApiModelProperty("页码，默认为1")
    private Integer pageNo;

    @ApiModelProperty(value = "页面大小，默认为10")
    private Integer pageSize;

    @ApiModelProperty(value = "排序字段，1为按评价时间排序，2为按热度排序", required = true)
    private Integer sortBy;
}
