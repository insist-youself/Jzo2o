package com.jzo2o.api.trade;

import com.jzo2o.api.trade.dto.response.ExecutionResultResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author itcast
 */
@FeignClient(contextId = "jzo2o-trade", value = "jzo2o-trade", path = "/trade/inner/refund-record")
public interface RefundRecordApi {

    /***
     * 统一收单交易退款接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param tradingOrderNo 支付单号
     * @param refundAmount 退款金额
     * @return
     */
    @PostMapping("refund")
    ExecutionResultResDTO refundTrading(@RequestParam("tradingOrderNo") Long tradingOrderNo,
                                        @RequestParam("refundAmount") BigDecimal refundAmount);
}
