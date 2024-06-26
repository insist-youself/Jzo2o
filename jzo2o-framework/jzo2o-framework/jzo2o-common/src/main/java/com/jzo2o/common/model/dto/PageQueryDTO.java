package com.jzo2o.common.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@ApiModel("分页查询数据")
public class PageQueryDTO {
    @ApiModelProperty("页码数")
    private Long pageNo=1L;
    @ApiModelProperty("每页条数")
    private Long pageSize=10L;
    @ApiModelProperty("排序字段1")
    private String orderBy1;
    @ApiModelProperty("排序字段1是否升序")
    private Boolean isAsc1 = false;

    @ApiModelProperty("排序字段2，排序顺序排在排序字段1后边，如果排序字段1未设置，该字段也可以排序")
    private String orderBy2;
    @ApiModelProperty("排序字段2是否升序")
    private Boolean isAsc2 = false;

    /**
     * 计算起始条数
     * @return
     */
    public Long calFrom() {
        return pageNo.intValue() * pageSize.intValue() * 1L;
    }
    @AllArgsConstructor
    @Getter
    public static class OrderBy {
        private String orderBy;
        private Boolean isAsc;
    }
}
