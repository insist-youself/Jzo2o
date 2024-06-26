package com.jzo2o.api.trade.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 程序执行结果
 *
 * @author itcast
 * @create 2023/9/7 15:41
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResultResDTO {

    /**
     * 支付服务退款单号
     */
    @ApiModelProperty("退款单号")
    private Long refundNo;

    /**
     * 第三方支付的退款单号
     */
    @ApiModelProperty("第三方支付的退款单号")
    private String refundId;

    /**
     * 退款状态，1：退款中，2：退款成功，3：退款失败
     */
    @ApiModelProperty("退款状态 0:发起退款 1：退款中，2：退款成功，3：退款失败")
    private Integer refundStatus;
}
