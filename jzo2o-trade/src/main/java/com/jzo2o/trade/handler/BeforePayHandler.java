package com.jzo2o.trade.handler;

import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.trade.model.domain.RefundRecord;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.model.dto.TradingDTO;

import java.math.BigDecimal;

/**
 * 交易前置处理接口
 *
 * @author itcast
 */
public interface BeforePayHandler {



    /***
     * 交易单参数校验
     * @param tradingEntity 交易订单
     * @return 是否符合要求
     */
    void checkCreateTrading(Trading tradingEntity);

    /***
     * QueryTrading交易单参数校验
     * @param trading 交易订单
     */
    void checkQueryTrading(Trading trading);


    /***
     * RefundTrading退款交易单参数校验
     * @param trading 交易订单
     * @param refundAmount 退款金额
     */
    void checkRefundTrading(Trading trading,BigDecimal refundAmount);


    /***
     * QueryRefundTrading交易单参数校验
     * @param refundRecord 退款记录
     */
    void checkQueryRefundTrading(RefundRecord refundRecord);

    /***
     * CloseTrading交易单参数校验
     * @param trading 交易订单
     */
    void checkCloseTrading(Trading trading);
}
