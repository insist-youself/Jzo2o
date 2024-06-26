package com.jzo2o.trade.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.trade.constant.Constants;
import com.jzo2o.trade.constant.TradingCacheConstant;
import com.jzo2o.trade.constant.TradingConstant;
import com.jzo2o.trade.enums.TradingEnum;
import com.jzo2o.trade.handler.BeforePayHandler;
import com.jzo2o.trade.handler.HandlerFactory;
import com.jzo2o.trade.handler.JsapiPayHandler;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.service.JsapiPayService;
import com.jzo2o.trade.service.TradingService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author itcast
 */
@Slf4j
@Service
public class JsapiPayServiceImpl implements JsapiPayService {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private TradingService tradingService;
    @Resource
    private BeforePayHandler beforePayHandler;

    @Override
    public Trading createJsapiTrading(Trading tradingEntity) {
        //获取付款中的记录
        Trading trading = tradingService.queryDuringTrading(tradingEntity.getProductAppId(),tradingEntity.getProductOrderNo(), tradingEntity.getTradingChannel());
        //付款中的记录直接返回
        if (ObjectUtil.isNotNull(trading)){
            return trading;
        }
        //交易前置处理：检测交易单参数
        beforePayHandler.checkCreateTrading(tradingEntity);
        tradingEntity.setEnableFlag(Constants.YES);
        tradingEntity.setTradingType(TradingConstant.TRADING_TYPE_FK);

        //对交易订单加锁
        Long productOrderNo = tradingEntity.getProductOrderNo();
        String key = TradingCacheConstant.CREATE_PAY + productOrderNo;
        RLock lock = redissonClient.getFairLock(key);
        try {
            //获取锁
            if (lock.tryLock(TradingCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)) {

                //交易前置处理：幂等性处理
//                this.beforePayHandler.idempotentCreateTrading(tradingEntity);

                //调用不同的支付渠道进行处理
                JsapiPayHandler jsapiPayHandler = HandlerFactory.get(tradingEntity.getTradingChannel(), JsapiPayHandler.class);
                jsapiPayHandler.createJsapiTrading(tradingEntity);

                //新增或更新交易数据
                boolean flag = this.tradingService.saveOrUpdate(tradingEntity);
                if (!flag) {
                    throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.SAVE_OR_UPDATE_FAIL.getValue());
                }

                return tradingEntity;
            }
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_PAY_FAIL.getValue());
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            log.error("Jsapi预创建异常: tradingDTO = {}", tradingEntity, e);
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_PAY_FAIL.getValue());
        } finally {
            lock.unlock();
        }
    }
}
