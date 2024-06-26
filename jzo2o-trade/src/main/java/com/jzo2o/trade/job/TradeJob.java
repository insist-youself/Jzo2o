package com.jzo2o.trade.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.jzo2o.common.constants.MqConstants;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.model.msg.TradeStatusMsg;
import com.jzo2o.rabbitmq.client.RabbitClient;
import com.jzo2o.trade.enums.TradingStateEnum;
import com.jzo2o.trade.handler.BasicPayHandler;
import com.jzo2o.trade.handler.HandlerFactory;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.model.dto.TradingDTO;
import com.jzo2o.trade.service.BasicPayService;
import com.jzo2o.trade.service.RefundRecordService;
import com.jzo2o.trade.service.TradingService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易任务，主要是查询订单的支付状态 和 退款的成功状态
 *
 * @author zzj
 * @version 1.0
 */
@Slf4j
@Component
public class TradeJob {

    @Value("${jzo2o.job.trading.count:100}")
    private Integer tradingCount;
    @Value("${jzo2o.job.refund.count:100}")
    private Integer refundCount;
    @Resource
    private TradingService tradingService;
    @Resource
    private RefundRecordService refundRecordService;
    @Resource
    private BasicPayService basicPayService;
    @Resource
    private RabbitClient rabbitClient;

    /**
     * 分片广播方式查询支付状态
     * 逻辑：每次最多查询{tradingCount}个未完成的交易单，交易单id与shardTotal取模，值等于shardIndex进行处理
     */
    @XxlJob("tradingJob")
    public void tradingJob() {
        // 分片参数
        int shardIndex = NumberUtil.max(XxlJobHelper.getShardIndex(), 0);
        int shardTotal = NumberUtil.max(XxlJobHelper.getShardTotal(), 1);

        List<Trading> list = this.tradingService.findListByTradingState(TradingStateEnum.FKZ, tradingCount);
        if (CollUtil.isEmpty(list)) {
            XxlJobHelper.log("查询到交易单列表为空！shardIndex = {}, shardTotal = {}", shardIndex, shardTotal);
            return;
        }

        //定义消息通知列表，只要是状态不为【付款中】就需要通知其他系统
        List<TradeStatusMsg> tradeMsgList = new ArrayList<>();
        for (Trading trading : list) {
            if (trading.getTradingOrderNo() % shardTotal != shardIndex) {
                continue;
            }
            try {
                //查询交易单
                TradingDTO tradingDTO = this.basicPayService.queryTradingResult(trading.getTradingOrderNo());
                if (TradingStateEnum.FKZ != tradingDTO.getTradingState()) {
                    TradeStatusMsg tradeStatusMsg = TradeStatusMsg.builder()
                            .tradingOrderNo(trading.getTradingOrderNo())
                            .productOrderNo(trading.getProductOrderNo())
                            .productAppId(trading.getProductAppId())
                            .transactionId(tradingDTO.getTransactionId())
                            .tradingChannel(tradingDTO.getTradingChannel())
                            .statusCode(tradingDTO.getTradingState().getCode())
                            .statusName(tradingDTO.getTradingState().name())
                            .info(tradingDTO.getMemo())//备注信息
                            .build();
                    tradeMsgList.add(tradeStatusMsg);
                }else{
                    //如果是未支付，需要判断下时间，超过20分钟未支付的订单需要关闭订单以及设置状态为QXDD
                   long between = LocalDateTimeUtil.between(trading.getCreateTime(), LocalDateTimeUtil.now(), ChronoUnit.MINUTES);
                    if (between >= 20) {
                        try {
                            basicPayService.closeTrading(trading.getTradingOrderNo());
                        } catch (Exception e) {
                            log.error("超过20分钟未支付自动关单出现异常,交易单号:{}",trading.getTradingOrderNo());
                        }
                    }
                }
            } catch (Exception e) {
                XxlJobHelper.log("查询交易单出错！shardIndex = {}, shardTotal = {}, trading = {}", shardIndex, shardTotal, trading, e);
            }
        }

        if (CollUtil.isEmpty(tradeMsgList)) {
            return;
        }

        //发送消息通知其他系统
        String msg = JSONUtil.toJsonStr(tradeMsgList);
        rabbitClient.sendMsg(MqConstants.Exchanges.TRADE, MqConstants.RoutingKeys.TRADE_UPDATE_STATUS, msg);
    }

    /**
     * 分片广播方式查询退款状态
     */
//    @XxlJob("refundJob")
//    public void refundJob() {
//        // 分片参数
//        int shardIndex = NumberUtil.max(XxlJobHelper.getShardIndex(), 0);
//        int shardTotal = NumberUtil.max(XxlJobHelper.getShardTotal(), 1);
//
//        List<RefundRecordEntity> list = this.refundRecordService.findListByRefundStatus(RefundStatusEnum.SENDING, refundCount);
//        if (CollUtil.isEmpty(list)) {
//            XxlJobHelper.log("查询到退款单列表为空！shardIndex = {}, shardTotal = {}", shardIndex, shardTotal);
//            return;
//        }
//
//        //定义消息通知列表，只要是状态不为【退款中】就需要通知其他系统
//        List<TradeStatusMsg> tradeMsgList = new ArrayList<>();
//
//        for (RefundRecordEntity refundRecord : list) {
//            if (refundRecord.getRefundNo() % shardTotal != shardIndex) {
//                continue;
//            }
//            try {
//                //查询退款单
//                RefundRecordDTO refundRecordDTO = this.basicPayService.queryRefundTrading(refundRecord.getRefundNo());
//                if (RefundStatusEnum.SENDING != refundRecordDTO.getRefundStatus()) {
//                    TradeStatusMsg tradeStatusMsg = TradeStatusMsg.builder()
//                            .tradingOrderNo(refundRecord.getTradingOrderNo())
//                            .productOrderNo(refundRecord.getProductOrderNo())
//                            .refundNo(refundRecord.getRefundNo())
//                            .statusCode(refundRecord.getRefundStatus().getCode())
//                            .statusName(refundRecord.getRefundStatus().name())
//                            .build();
//                    tradeMsgList.add(tradeStatusMsg);
//                }
//            } catch (Exception e) {
//                XxlJobHelper.log("查询退款单出错！shardIndex = {}, shardTotal = {}, refundRecord = {}", shardIndex, shardTotal, refundRecord, e);
//            }
//        }
//
//        if (CollUtil.isEmpty(tradeMsgList)) {
//            return;
//        }
//
//        //发送消息通知其他系统
//        String msg = JSONUtil.toJsonStr(tradeMsgList);
//        this.mqFeign.sendMsg(Constants.MQ.Exchanges.TRADE, Constants.MQ.RoutingKeys.REFUND_UPDATE_STATUS, msg);
//    }
}
