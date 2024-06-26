package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("评价系统token信息")
public class EvaluationTokenDto {

    /**
     * 评价系统token
     */
    @ApiModelProperty("评价系统token")
    private String token;
}