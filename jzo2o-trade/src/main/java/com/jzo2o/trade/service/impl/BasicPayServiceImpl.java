package com.jzo2o.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.jzo2o.api.trade.dto.response.ExecutionResultResDTO;
import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.trade.constant.Constants;
import com.jzo2o.trade.constant.TradingCacheConstant;
import com.jzo2o.trade.enums.RefundStatusEnum;
import com.jzo2o.trade.enums.TradingEnum;
import com.jzo2o.trade.enums.TradingStateEnum;
import com.jzo2o.trade.handler.BasicPayHandler;
import com.jzo2o.trade.handler.BeforePayHandler;
import com.jzo2o.trade.handler.HandlerFactory;
import com.jzo2o.trade.model.domain.RefundRecord;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.model.dto.RefundRecordDTO;
import com.jzo2o.trade.model.dto.TradingDTO;
import com.jzo2o.trade.service.BasicPayService;
import com.jzo2o.trade.service.RefundRecordService;
import com.jzo2o.trade.service.TradingService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 支付的基础功能
 */
@Slf4j
@Service
public class BasicPayServiceImpl implements BasicPayService {

    @Resource
    private BeforePayHandler beforePayHandler;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private TradingService tradingService;
    @Resource
    private RefundRecordService refundRecordService;
    @Resource
    private IdentifierGenerator identifierGenerator;


    @Override
    public TradingDTO queryTradingResult(Long tradingOrderNo) throws CommonException {
        //通过单号查询交易单数据
        Trading trading = this.tradingService.findTradByTradingOrderNo(tradingOrderNo);
        if(ObjectUtil.isNull(trading)){
            return null;
        }
        //如果已付款或已取消直接返回
        if(StrUtil.equalsAny(trading.getTradingState().getValue(),TradingStateEnum.YJS.getValue(),TradingStateEnum.QXDD.getValue())){
            return BeanUtil.toBean(trading,TradingDTO.class);
        }
        //查询前置处理：检测交易单参数
        this.beforePayHandler.checkQueryTrading(trading);
        //支付状态
        TradingStateEnum tradingState = trading.getTradingState();
        //如果支付成功或支付取消就直接返回
        if (ObjectUtil.equal(tradingState, TradingStateEnum.YJS) || ObjectUtil.equal(tradingState, TradingStateEnum.QXDD)) {
            return BeanUtil.toBean(trading, TradingDTO.class);
        }
        String key = TradingCacheConstant.QUERY_PAY + tradingOrderNo;
        RLock lock = redissonClient.getFairLock(key);
        try {
            //获取锁
            if (lock.tryLock(TradingCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)) {
                //选取不同的支付渠道实现
//                BasicPayHandler handler = HandlerFactory.get(trading.getTradingChannel(), BasicPayHandler.class);
//                Boolean result = handler.queryTrading(trading);
                //这里个体无法调用微信支付，手动赋值
                Boolean result = true;
                if (result) {
                    //如果交易单已经完成，需要将二维码数据删除，节省数据库空间，如果有需要可以再次生成
                    if (ObjectUtil.equal(trading.getTradingState(), TradingStateEnum.YJS) || ObjectUtil.equal(trading.getTradingState(), TradingStateEnum.QXDD)) {
                        trading.setQrCode("");
                    }
                    trading.setTradingState(TradingStateEnum.YJS);
                    trading.setResultCode("SUCCESS");
                    trading.setResultMsg("支付成功");
                    //{"amount":{"currency":"CNY","discount_refund":0,"from":[],"payer_refund":100,"payer_total":100,"refund":100,"refund_fee":1,"settlement_refund":100,"settlement_total":100,"total":100},"channel":"ORIGINAL","create_time":"2023-11-02T18:23:23+08:00","funds_account":"AVAILABLE","out_refund_no":"1720023788378394625","out_trade_no":"1720023598087016449","promotion_detail":[],"refund_id":"50310207402023110203907454908","status":"SUCCESS","success_time":"2023-11-02T18:23:30+08:00","transaction_id":"4200001974202311028474971242","user_received_account":"支付用户零钱"}
                    // 将LocalDateTime对象转换为字符串 2023-11-01T21:59:17+08:00
                    String formattedDateTime = LocalDateTime.now()
                            .atOffset(ZoneOffset.ofHours(8))
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
                    trading.setResultJson("{\"amount\":{\"currency\":\"CNY\"," +
                            "\"payer_currency\":\"CNY\",\"payer_total\":1,\"total\":1}," +
                            "\"appid\":\"wx6592a2db3f85ed25\",\"attach\":\"\"," +
                            "\"bank_type\":\"OTHERS\",\"mchid\":\"1561414331\"," +
                            "\"out_trade_no\":\"1728962152038494209\"," +
                            "\"payer\":{\"openid\":\"otdlR4_yoPINKcnvtdPlSUnk2XAc\"}," +
                            "\"promotion_detail\":[]," +
                            "\"success_time\":\""+ formattedDateTime +"\"," +
                            "\"trade_state\":\"SUCCESS\",\"trade_state_desc\":\"支付成功\"," +
                            "\"trade_type\":\"NATIVE\",\"transaction_id\":\"4200001973202311273981614609\"}");
                    //更新数据
                    this.tradingService.saveOrUpdate(trading);
                }
                return BeanUtil.toBean(trading, TradingDTO.class);
            }
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_QUERY_FAIL.getValue());
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询交易单数据异常: trading = {}", trading, e);
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_QUERY_FAIL.getValue());
        } finally {
            lock.unlock();
        }
    }

//    @Override
//    @Transactional
//    public ExecutionResultResDTO refundTradingByTradingOrderNo(Long tradingOrderNo, BigDecimal refundAmount) throws CommonException {
//        //根据业务订单号查看交易单信息
//        Trading trading = this.tradingService.findTradByTradingOrderNo(tradingOrderNo);
//        if(ObjectUtil.isEmpty(trading)){
//            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NOT_FOUND.getValue());
//        }
//        //只有已付款的交易单方可退款
//        if(ObjectUtil.notEqual(TradingStateEnum.YJS,trading.getTradingState())){
//            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.REFUND_FAIL.getValue());
//        }
//
//        ExecutionResultResDTO executionResultResDTO = refundTrading(trading.getTradingOrderNo(), refundAmount);
//        return  executionResultResDTO;
//    }

    @Override
    @Transactional
    public RefundRecord refundTrading(Long tradingOrderNo, BigDecimal refundAmount) throws CommonException {
        //通过单号查询交易单数据
        Trading trading = this.tradingService.findTradByTradingOrderNo(tradingOrderNo);
        //入库前置检查
        this.beforePayHandler.checkRefundTrading(trading,refundAmount);
        String key = TradingCacheConstant.REFUND_PAY + trading.getTradingOrderNo();
        RLock lock = redissonClient.getFairLock(key);
        try {
            //获取锁
            if (lock.tryLock(TradingCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)) {

                //对于退款中的记录需要同步退款状态
                syncRefundResult(tradingOrderNo);
                //查询退款记录
                List<RefundRecord> refundRecordList = this.refundRecordService.findByTradingOrderNo(trading.getTradingOrderNo());
                //取出退款成功或退款中的记录
                List<RefundRecord> collect = refundRecordList.stream().filter(r -> StrUtil.equalsAny(r.getRefundStatus().getValue(),RefundStatusEnum.SENDING.getValue(),RefundStatusEnum.SUCCESS.getValue())).collect(Collectors.toList());
                //当没有退款成功和退款中的记录时方可继续退款
                if(ObjectUtil.isEmpty(collect)){
                    //设置退款金额
                    trading.setRefund(refundAmount);

                    RefundRecord refundRecord = new RefundRecord();
                    //退款单号
                    refundRecord.setRefundNo(Convert.toLong(this.identifierGenerator.nextId(refundRecord)));
                    refundRecord.setTradingOrderNo(trading.getTradingOrderNo());
                    refundRecord.setProductOrderNo(trading.getProductOrderNo());
                    refundRecord.setProductAppId(trading.getProductAppId());
                    refundRecord.setRefundAmount(refundAmount);
                    refundRecord.setEnterpriseId(trading.getEnterpriseId());
                    refundRecord.setTradingChannel(trading.getTradingChannel());
                    refundRecord.setTotal(trading.getTradingAmount());
                    //初始状态为退款中
                    refundRecord.setRefundStatus(RefundStatusEnum.APPLY_REFUND);
                    this.refundRecordService.save(refundRecord);
                    //设置交易单是退款订单
                    trading.setIsRefund(Constants.YES);
                    this.tradingService.saveOrUpdate(trading);

                    //请求第三方退款
                    //选取不同的支付渠道实现
                    BasicPayHandler handler = HandlerFactory.get(refundRecord.getTradingChannel(), BasicPayHandler.class);
                    Boolean result = handler.refundTrading(refundRecord);
                    if (result) {
                        //更新退款记录数据
                        this.refundRecordService.saveOrUpdate(refundRecord);
                    }
                    return refundRecord;
                }
                //取出第一条记录返回
                RefundRecord first = CollectionUtil.getFirst(refundRecordList);
                if(ObjectUtil.isNotNull(first)){
                    return first;
                }

            }
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_QUERY_FAIL.getValue());
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询交易单数据异常:{}", ExceptionUtil.stacktraceToString(e));
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_QUERY_FAIL.getValue());
        } finally {
            lock.unlock();
        }
    }

    /***
     * 对于退款中的记录需要同步退款状态
     * @param tradingOrderNo 交易单号
     */
    @Override
    public void syncRefundResult(Long tradingOrderNo) throws CommonException{
        //查询退款记录
        List<RefundRecord> refundRecordList = this.refundRecordService.findByTradingOrderNo(tradingOrderNo);
        //存在退款中记录
        List<RefundRecord> collect = refundRecordList.stream().filter(r -> r.getRefundStatus().equals(RefundStatusEnum.SENDING)).collect(Collectors.toList());

        if (ObjectUtil.isNotEmpty(collect)) {
            collect.forEach(v->{
                queryRefundTrading(v.getRefundNo());
            });
        }

    }
    @Override
    public RefundRecordDTO queryRefundTrading(Long refundNo) throws CommonException {
        //通过单号查询交易单数据
        RefundRecord refundRecord = this.refundRecordService.findByRefundNo(refundNo);
        //查询前置处理
        this.beforePayHandler.checkQueryRefundTrading(refundRecord);

        String key = TradingCacheConstant.REFUND_QUERY_PAY + refundNo;
        RLock lock = redissonClient.getFairLock(key);
        try {
            //获取锁
            if (lock.tryLock(TradingCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)) {

                //选取不同的支付渠道实现
                BasicPayHandler handler = HandlerFactory.get(refundRecord.getTradingChannel(), BasicPayHandler.class);
                Boolean result = handler.queryRefundTrading(refundRecord);
                if (result) {
                    //更新数据
                    this.refundRecordService.saveOrUpdate(refundRecord);
                }
                return BeanUtil.toBean(refundRecord, RefundRecordDTO.class);
            }
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.REFUND_FAIL.getValue());
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询退款交易单数据异常: refundRecord = {}", refundRecord, e);
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.REFUND_FAIL.getValue());
        } finally {
            lock.unlock();
        }
    }

    /***
     * 关闭交易单
     * @param tradingOrderNo 交易单号
     * @return 是否成功
     */
    @Override
    public Boolean closeTrading(Long tradingOrderNo) throws CommonException {
        //通过单号查询交易单数据
        Trading trading = this.tradingService.findTradByTradingOrderNo(tradingOrderNo);
        if (ObjectUtil.isEmpty(trading)) {
            return true;
        }

        //入库前置检查
        this.beforePayHandler.checkCloseTrading(trading);

        String key = TradingCacheConstant.CLOSE_PAY + trading.getTradingOrderNo();
        RLock lock = redissonClient.getFairLock(key);
        try {
            //获取锁
            if (lock.tryLock(TradingCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)) {

                //选取不同的支付渠道实现
                BasicPayHandler handler = HandlerFactory.get(trading.getTradingChannel(), BasicPayHandler.class);
                Boolean result = handler.closeTrading(trading);
                if (result) {
                    trading.setQrCode("");
                    this.tradingService.saveOrUpdate(trading);
                }
                return true;
            }
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_QUERY_FAIL.getValue());
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询交易单数据异常:{}", ExceptionUtil.stacktraceToString(e));
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_QUERY_FAIL.getValue());
        } finally {
            lock.unlock();
        }
    }
}
