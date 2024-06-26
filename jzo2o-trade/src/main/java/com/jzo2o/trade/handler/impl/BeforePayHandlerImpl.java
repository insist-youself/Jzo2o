package com.jzo2o.trade.handler.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.jzo2o.api.trade.dto.response.ExecutionResultResDTO;
import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.trade.enums.RefundStatusEnum;
import com.jzo2o.trade.enums.TradingEnum;
import com.jzo2o.trade.enums.TradingStateEnum;
import com.jzo2o.trade.handler.BasicPayHandler;
import com.jzo2o.trade.handler.BeforePayHandler;
import com.jzo2o.trade.handler.HandlerFactory;
import com.jzo2o.trade.model.domain.RefundRecord;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.model.dto.TradingDTO;
import com.jzo2o.trade.service.RefundRecordService;
import com.jzo2o.trade.service.TradingService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易前置处理接口
 *
 * @author zzj
 * @version 1.0
 */
@Component
public class BeforePayHandlerImpl implements BeforePayHandler {

    @Resource
    private TradingService tradingService;
    @Resource
    private IdentifierGenerator identifierGenerator;
    @Resource
    private RefundRecordService refundRecordService;


    @Override
    public void checkCreateTrading(Trading tradingEntity) {
        //校验不为为空，订单备注、订单号、企业号、交易金额、支付渠道
        boolean flag = ObjectUtil.isAllNotEmpty(tradingEntity,
                tradingEntity.getMemo(),
                tradingEntity.getProductOrderNo(),
                tradingEntity.getEnterpriseId(),
                tradingEntity.getTradingAmount(),
                tradingEntity.getTradingChannel());
        //金额不能小于等于0
        boolean flag2 = !NumberUtil.isLessOrEqual(tradingEntity.getTradingAmount(), BigDecimal.valueOf(0));
        if (!flag || !flag2) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.CONFIG_ERROR.getValue());
        }

        List<Trading> tradings = tradingService.queryByProductOrder(tradingEntity.getProductAppId(),tradingEntity.getProductOrderNo());
        if (ObjectUtil.isEmpty(tradings)) {
            //新交易单，生成交易号
            tradingEntity.setTradingOrderNo((Long) identifierGenerator.nextId(tradingEntity));
            return ;
        }
        //找到已付款的记录
        Trading finishedTrading = tradingService.findFinishedTrading(tradingEntity.getProductAppId(),tradingEntity.getProductOrderNo());
        if (ObjectUtil.isNotEmpty(finishedTrading)) {
            //存在已付款单子直接抛出重复支付异常
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.TRADING_STATE_SUCCEED.getValue());
        }
        //找到该支付渠道支付中的单子
        Trading trading = tradingService.queryDuringTrading(tradingEntity.getProductAppId(),tradingEntity.getProductOrderNo(), tradingEntity.getTradingChannel());
        if (ObjectUtil.isNotEmpty(trading)) {
            //存在相同支付渠道的付款中单子
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.TRADING_STATE_PAYING.getValue());
        }
        //新交易单，生成交易号
        tradingEntity.setTradingOrderNo((Long) identifierGenerator.nextId(tradingEntity));
    }

    @Override
    public void checkQueryTrading(Trading trading) {
        if (ObjectUtil.isEmpty(trading)) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NOT_FOUND.getValue());
        }

    }


    @Override
    public void checkRefundTrading(Trading trading,BigDecimal refundAmount) {
        if (ObjectUtil.isEmpty(trading)) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NOT_FOUND.getValue());
        }

        if (trading.getTradingState() != TradingStateEnum.YJS) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.NATIVE_REFUND_FAIL.getValue());
        }

        //退款总金额不可超实付总金额
        if (NumberUtil.isGreater(refundAmount, trading.getTradingAmount())) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.BASIC_REFUND_OUT_FAIL.getValue());
        }

    }

    @Override
    public void checkQueryRefundTrading(RefundRecord refundRecord) {
        if (ObjectUtil.isEmpty(refundRecord)) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.REFUND_NOT_FOUND.getValue());
        }

        if (ObjectUtil.equals(refundRecord.getRefundStatus(), RefundStatusEnum.SUCCESS)) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.REFUND_ALREADY_COMPLETED.getValue());
        }
    }

    @Override
    public void checkCloseTrading(Trading trading) {
        if (ObjectUtil.notEqual(TradingStateEnum.FKZ, trading.getTradingState())) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, TradingEnum.CLOSE_FAIL.getValue());
        }
    }
}
