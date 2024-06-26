package com.jzo2o.trade.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易单状态响应数据
 *
 * @author itcast
 * @create 2023/9/11 10:41
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("交易单状态响应数据")
public class TradingStateResDTO {

    @ApiModelProperty(value = "交易单状态，2：付款中，4：已结算")
    private Integer tradingState;
}
