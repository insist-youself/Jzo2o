package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全部评价配置信息
 *
 * @author itcast
 * @create 2023/9/23 10:46
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("全部评价配置信息")
public class AllEvaluationSystemInfoResDTO {
    @ApiModelProperty("服务项评价配置信息")
    private EvaluationSystemInfoResDTO serveItemEvaluationSystemInfo;

    @ApiModelProperty("师傅评价配置信息")
    private EvaluationSystemInfoResDTO serveProviderEvaluationSystemInfo;
}
