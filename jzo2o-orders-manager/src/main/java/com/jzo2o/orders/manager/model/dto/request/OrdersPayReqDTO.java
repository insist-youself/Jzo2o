package com.jzo2o.orders.manager.model.dto.request;

import com.jzo2o.api.trade.enums.PayChannelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 订单支付请求体
 *
 * @author itcast
 * @create 2023/9/4 10:00
 **/
@Data
@ApiModel("订单支付请求体")
public class OrdersPayReqDTO {
    @ApiModelProperty(value = "支付渠道", required = true)
    @NotNull(message = "支付渠道不能为空")
    private PayChannelEnum tradingChannel;
}
