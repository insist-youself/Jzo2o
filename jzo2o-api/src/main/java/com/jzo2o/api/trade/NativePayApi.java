package com.jzo2o.api.trade;

import com.jzo2o.api.trade.dto.request.NativePayReqDTO;
import com.jzo2o.api.trade.dto.response.NativePayResDTO;
import com.jzo2o.api.trade.enums.PayChannelEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author itcast
 */
@FeignClient(contextId = "jzo2o-trade", value = "jzo2o-trade", path = "/trade/inner/native")
public interface NativePayApi {

    /***
     * 扫码支付，收银员通过收银台或商户后台调用此接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。
     *
     * @param nativePayDTO 扫码支付提交参数
     * @return 扫码支付响应数据，其中包含二维码路径
     */
    @PostMapping
    NativePayResDTO createDownLineTrading(@RequestBody NativePayReqDTO nativePayDTO);

//    /**
//     * 根据订单id和支付方式查询交易单
//     *
//     * @param productOrderNo 订单号
//     * @param payChannelEnum 支付方式
//     * @return 交易单
//     */
//    @GetMapping("queryByProductOrderNoAndTradingChannel")
//    NativePayResDTO queryByProductOrderNoAndTradingChannel(@RequestParam("productOrderNo") Long productOrderNo,
//                                                           @RequestParam("payChannelEnum") PayChannelEnum payChannelEnum);
}
