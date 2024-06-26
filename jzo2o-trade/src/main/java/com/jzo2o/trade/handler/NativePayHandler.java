package com.jzo2o.trade.handler;

import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.trade.model.domain.Trading;

/**
 * @author itcast
 * @ClassName NativePayHandler.java
 * @Description Native支付方式Handler：商户生成二维码，用户扫描支付
 */
public interface NativePayHandler {


    /***
     * @description 统一收单线下交易预创建
     * 收银员通过收银台或商户后台调用此接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。
     * @param tradingEntity 交易单
     */
    void createDownLineTrading(Trading tradingEntity) throws CommonException;

}
