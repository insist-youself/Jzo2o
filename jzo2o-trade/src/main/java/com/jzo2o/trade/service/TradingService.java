package com.jzo2o.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.api.trade.enums.PayChannelEnum;
import com.jzo2o.trade.enums.TradingStateEnum;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.model.dto.TradingDTO;

import java.util.List;

/**
 * @Description：交易订单表 服务类
 */
public interface TradingService extends IService<Trading> {

    /***
     * 按交易单号查询交易单
     *
     * @param tradingOrderNo 交易单号
     * @return 交易单数据
     */
    Trading findTradByTradingOrderNo(Long tradingOrderNo);



    /***
     * 按交易状态查询交易单，按照时间正序排序
     * @param tradingState 状态
     * @param count 查询数量，默认查询10条
     * @return 交易单数据列表
     */
    List<Trading> findListByTradingState(TradingStateEnum tradingState, Integer count);


    /**
     * 根据订单id和支付方式查询付款中的交易单
     * @param productAppId 业务系统标识
     * @param productOrderNo 订单号
     * @param tradingChannel 支付渠道代码
     * @return 交易单
     */
    Trading queryDuringTrading(String productAppId,Long productOrderNo, String tradingChannel);

    /**
     * 根据订单id查询交易单
     *
     * @param productAppId 业务系统标识
     * @param productOrderNo 订单号
     * @return 交易单
     */
    List<Trading> queryByProductOrder(String productAppId,Long productOrderNo);

    /**
     * 根据订单id查询已付款的交易单
     * @param productAppId 业务系统标识
     * @param productOrderNo 订单id
     * @return 交易单
     */
    Trading findFinishedTrading(String productAppId,Long productOrderNo);
}
