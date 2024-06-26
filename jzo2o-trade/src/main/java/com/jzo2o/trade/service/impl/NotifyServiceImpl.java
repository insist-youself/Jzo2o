package com.jzo2o.trade.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.constants.MqConstants;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.model.msg.TradeStatusMsg;
import com.jzo2o.rabbitmq.client.RabbitClient;
import com.jzo2o.trade.constant.TradingCacheConstant;
import com.jzo2o.trade.constant.TradingConstant;
import com.jzo2o.trade.enums.TradingStateEnum;
import com.jzo2o.trade.handler.alipay.AlipayConfig;
import com.jzo2o.trade.handler.wechat.WechatPayHttpClient;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.service.NotifyService;
import com.jzo2o.trade.service.TradingService;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 支付成功的通知处理
 *
 * @author itcast
 */
@Slf4j
@Service
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private TradingService tradingService;
    @Resource
    private RedissonClient redissonClient;
    //    @Resource
//    private MQFeign mqFeign;
    @Resource
    private RabbitClient rabbitClient;

    @Override
    public void wxPayNotify(NotificationRequest request, Long enterpriseId) throws CommonException {
        // 查询配置
        WechatPayHttpClient client = WechatPayHttpClient.get(enterpriseId);

        JSONObject jsonData;

        //验证签名，确保请求来自微信
        try {
            //确保在管理器中存在自动更新的商户证书
            client.createHttpClient();

            CertificatesManager certificatesManager = CertificatesManager.getInstance();
            Verifier verifier = certificatesManager.getVerifier(client.getMchId());

            //验签和解析请求数据
            NotificationHandler notificationHandler = new NotificationHandler(verifier, client.getApiV3Key().getBytes(StandardCharsets.UTF_8));
            Notification notification = notificationHandler.parse(request);
            //通知的类型，支付成功通知的类型为TRANSACTION.SUCCESS，只处理此类通知
            if (!StrUtil.equals("TRANSACTION.SUCCESS", notification.getEventType())) {
                return;
            }

            //获取解密后的数据
            jsonData = JSONUtil.parseObj(notification.getDecryptData());
        } catch (Exception e) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, "验签失败");
        }

        //交易状态
        String tradeStateFromWX = jsonData.getStr("trade_state");
        /**
         交易状态，枚举值：
         SUCCESS：支付成功
         REFUND：转入退款
         NOTPAY：未支付
         CLOSED：已关闭
         REVOKED：已撤销（付款码支付）
         USERPAYING：用户支付中（付款码支付）
         PAYERROR：支付失败(其他原因，如银行返回失败)
         */
        String tradeStatus = TradingStateEnum.FKZ.getValue();
        if (StrUtil.equalsAny(tradeStateFromWX, TradingConstant.WECHAT_TRADE_CLOSED, TradingConstant.WECHAT_TRADE_REVOKED)) {
            tradeStatus = TradingStateEnum.QXDD.getValue();
            //支付成功或转入退款的更新为已付款
        } else if (StrUtil.equalsAny(tradeStateFromWX, TradingConstant.WECHAT_TRADE_SUCCESS, TradingConstant.WECHAT_TRADE_REFUND)) {
            tradeStatus = TradingStateEnum.YJS.getValue();
        } else if (StrUtil.equalsAny(tradeStateFromWX, TradingConstant.WECHAT_TRADE_PAYERROR)) {
            tradeStatus = TradingStateEnum.FKSB.getValue();
        }

        //交易单号
        Long tradingOrderNo = jsonData.getLong("out_trade_no");
        log.info("微信支付通知：tradingOrderNo = {}, data = {}", tradingOrderNo, jsonData);

        //更新交易单
        this.updateTrading(tradingOrderNo, jsonData.getStr("transaction_id"),tradeStatus, jsonData.getStr("trade_state_desc"), jsonData.toString());
    }

    private void updateTrading(Long tradingOrderNo, String transactionId,String tradeStatus, String resultMsg, String resultJson) {
        try {
                Trading trading = this.tradingService.findTradByTradingOrderNo(tradingOrderNo);
                trading.setTradingState(TradingStateEnum.valueOf(tradeStatus));
                //清空二维码数据
                trading.setQrCode("");
                trading.setTransactionId(transactionId);
                trading.setResultMsg(resultMsg);
                trading.setResultJson(resultJson);
                this.tradingService.saveOrUpdate(trading);

                // 发消息通知其他系统
                TradeStatusMsg tradeStatusMsg = TradeStatusMsg.builder()
                        .tradingOrderNo(trading.getTradingOrderNo())
                        .productOrderNo(trading.getProductOrderNo())
                        .productAppId(trading.getProductAppId())
                        .transactionId(trading.getTransactionId())
                        .tradingChannel(trading.getTradingChannel())
                        .statusCode(TradingStateEnum.YJS.getCode())
                        .statusName(TradingStateEnum.YJS.name())
                        .info(trading.getMemo())//备注信息
                        .build();

                String msg = JSONUtil.toJsonStr(Collections.singletonList(tradeStatusMsg));
                rabbitClient.sendMsg(MqConstants.Exchanges.TRADE, MqConstants.RoutingKeys.TRADE_UPDATE_STATUS, msg);
                return;
        } catch (Exception e) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, "处理业务失败");
        }
    }

    @Override
    public void aliPayNotify(HttpServletRequest request, Long enterpriseId) throws CommonException {
        //获取参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> param = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            param.put(entry.getKey(), StrUtil.join(",", entry.getValue()));
        }

        String tradeStatusFromAli = param.get("trade_status");
        String tradeStatus = TradingStateEnum.FKZ.getValue();
        if (StrUtil.equals(TradingConstant.ALI_TRADE_CLOSED, tradeStatusFromAli)) {
            //支付取消：TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
            tradeStatus = TradingStateEnum.QXDD.getValue();
        } else if (StrUtil.equalsAny(tradeStatusFromAli, TradingConstant.ALI_TRADE_SUCCESS, TradingConstant.ALI_TRADE_FINISHED)) {
            // TRADE_SUCCESS（交易支付成功）
            // TRADE_FINISHED（交易结束，不可退款）
            tradeStatus = TradingStateEnum.YJS.getValue();
        }

        //查询配置
        Config config = AlipayConfig.getConfig(enterpriseId);
        Factory.setOptions(config);
        try {
            Boolean result = Factory
                    .Payment
                    .Common().verifyNotify(param);
            if (!result) {
                throw new CommonException(ErrorInfo.Code.TRADE_FAILED, "验签失败");
            }
        } catch (Exception e) {
            throw new CommonException(ErrorInfo.Code.TRADE_FAILED, "验签失败");
        }

        //获取交易单号
        Long tradingOrderNo = Convert.toLong(param.get("out_trade_no"));
        String transactionId = param.get("trade_no");
        //更新交易单
        this.updateTrading(tradingOrderNo, transactionId, tradeStatus,"", JSONUtil.toJsonStr(param));
    }
}
