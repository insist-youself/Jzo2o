package com.jzo2o.trade.handler;

import com.jzo2o.trade.model.domain.Trading;

/**
 * jsapi下单处理
 *
 * @author itcast
 */
public interface JsapiPayHandler {

    /**
     * 创建交易
     *
     * @param tradingEntity 交易单
     */
    void createJsapiTrading(Trading tradingEntity);
}
