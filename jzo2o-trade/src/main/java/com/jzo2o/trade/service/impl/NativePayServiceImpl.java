package com.jzo2o.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.api.trade.dto.response.NativePayResDTO;
import com.jzo2o.api.trade.enums.PayChannelEnum;
import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.trade.constant.Constants;
import com.jzo2o.trade.constant.TradingCacheConstant;
import com.jzo2o.trade.constant.TradingConstant;
import com.jzo2o.trade.enums.TradingEnum;
import com.jzo2o.trade.enums.TradingStateEnum;
import com.jzo2o.trade.handler.BasicPayHandler;
import com.jzo2o.trade.handler.BeforePayHandler;
import com.jzo2o.trade.handler.HandlerFactory;
import com.jzo2o.trade.handler.NativePayHandler;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.service.BasicPayService;
import com.jzo2o.trade.service.NativePayService;
import com.jzo2o.trade.service.QRCodeService;
import com.jzo2o.trade.service.TradingService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Native支付方式Face接口：商户生成二维码，用户扫描支付
 *
 * @author itcast
 */
@Service
@Slf4j
public class NativePayServiceImpl implements NativePayService {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private TradingService tradingService;
    @Resource
    private BeforePayHandler beforePayHandler;
    @Resource
    private QRCodeService qrCodeService;
    @Resource
    private BasicPayService basicPayService;
    /**
     * 切换支付渠道自动关单
     * @param tradingChannel 要切换的目标支付渠道
     * @param productAppId 业务系统标识
     * @param productOrderNo 业务订单号
     */
    private void changeChannelAndCloseTrading(String productAppId,Long productOrderNo,String tradingChannel){
        //根据订单号查询交易单
        List<Trading> yjsTradByProductOrderNo = tradingService.queryByProductOrder(productAppId,productOrderNo);

        yjsTradByProductOrderNo.forEach(v->{
            //与目标支付渠道不同的渠道且支付中的进行关单
            if(ObjectUtil.notEqual(v.getTradingChannel(),tradingChannel) &&
                v.getTradingState().equals(TradingStateEnum.FKZ)){
                //关单
                Boolean result = basicPayService.closeTrading(v.getTradingOrderNo());
                log.info("业务系统:{},业务订单:{},切换交易订单:{}的支付渠道为:{},关闭其它支付渠道:{}",productAppId,productOrderNo,v.getTradingOrderNo(),tradingChannel,v.getTradingChannel());
            }
        });

    }

    @Override
    public Trading createDownLineTrading(boolean changeChannel,Trading tradingEntity) {
        //获取付款中的记录
        Trading trading = tradingService.queryDuringTrading(tradingEntity.getProductAppId(),tradingEntity.getProductOrderNo(), tradingEntity.getTradingChannel());
        //如果切换二维码需要查询其它支付渠道付款中的交易单进行退款操作
        if(changeChannel){
            changeChannelAndCloseTrading(tradingEntity.getProductAppId(),tradingEntity.getProductOrderNo(),tradingEntity.getTradingChannel());
        }
        //付款中的记录直接返回无需生成二维码
        if (ObjectUtil.isNotNull(trading)){
            return trading;
        }
        //交易前置处理：检测交易单参数
        beforePayHandler.checkCreateTrading(tradingEntity);

        tradingEntity.setTradingType(TradingConstant.TRADING_TYPE_FK);
        tradingEntity.setEnableFlag(Constants.YES);

        //对交易订单加锁
        Long productOrderNo = tradingEntity.getProductOrderNo();
        String key = TradingCacheConstant.CREATE_PAY + productOrderNo;
        RLock lock = redissonClient.getFairLock(key);
        try {
            //获取锁
            if (lock.tryLock(TradingCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)) {
                //交易前置处理：幂等性处理
//                this.beforePayHandler.idempotentCreateTrading(tradingEntity);

                //&#x8C03;&#x7528;&#x4E0D;&#x540C;&#x7684;&#x652F;&#x4ED8;&#x6E20;&#x9053;&#x8FDB;&#x884C;&#x5904;&#x7406;
                PayChannelEnum payChannel = PayChannelEnum.valueOf(tradingEntity.getTradingChannel());
                NativePayHandler nativePayHandler = HandlerFactory.get(payChannel, NativePayHandler.class);
                nativePayHandler.createDownLineTrading(tradingEntity);

                //生成统一收款二维码
//                String placeOrderMsg = tradingEntity.getPlaceOrderMsg();
//                String qrCode = this.qrCodeService.generate(placeOrderMsg, payChannel);
                // 由于个体无法使用支付相关功能，这里我们自定义一下响应参数。
                String qrCode = "https://jzo2o-oss-001.oss-cn-hangzhou.aliyuncs.com/d4c42b68-ff08-4473-94ea-c2c2c4ccc9d4.png";
                tradingEntity.setQrCode(qrCode);
                //4200001991202311013890714230
                Random random = new Random();
                // 获取当前时间
                LocalDateTime currentTime = LocalDateTime.now();
                // 定义日期时间格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                // 格式化当前时间为字符串
                String currentTimeString = currentTime.format(formatter);
                tradingEntity.setTransactionId("42000020" + String.valueOf(random.nextInt(99) + 0) + currentTimeString
                        + String.valueOf(random.nextInt(9999) + 0));
                //指定交易状态为付款中
                tradingEntity.setTradingState(TradingStateEnum.FKZ);
                //新增交易数据
                boolean flag = this.tradingService.save(tradingEntity);
                if (!flag) {
                    throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.SAVE_OR_UPDATE_FAIL.getValue());
                }

                return tradingEntity;
            }
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_PAY_FAIL.getValue());
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            log.error("统一收单线下交易预创建异常:{}", ExceptionUtil.stacktraceToString(e));
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_PAY_FAIL.getValue());
        } finally {
            lock.unlock();
        }
    }

}
