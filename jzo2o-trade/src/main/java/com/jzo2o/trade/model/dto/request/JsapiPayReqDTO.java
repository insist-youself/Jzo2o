package com.jzo2o.trade.model.dto.request;

import com.jzo2o.api.trade.enums.PayChannelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * jsapi提交支付请求对象
 *
 * @author zzj
 * @version 1.0
 */
@Data
public class JsapiPayReqDTO {

    @ApiModelProperty(value = "openId标识", required = true)
    private String openId;
    @ApiModelProperty(value = "商户号", required = true)
    private Long enterpriseId;
    @ApiModelProperty(value = "业务系统应用标识", required = true)
    private String productAppId;
    @ApiModelProperty(value = "业务系统订单号", required = true)
    private Long productOrderNo;
    @ApiModelProperty(value = "支付渠道", required = true)
    private PayChannelEnum tradingChannel;
    @ApiModelProperty(value = "交易金额，单位：元", required = true)
    private BigDecimal tradingAmount;
    @ApiModelProperty(value = "备注，如：运费", required = true)
    private String memo;
}
