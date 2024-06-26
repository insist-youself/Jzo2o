package com.jzo2o.trade.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jzo2o.trade.enums.TradingStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author itcast
 * @Description：交易订单表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("trading")
public class Trading implements Serializable {
    private static final long serialVersionUID = -3427581867070559590L;

    /**
     * 主键
     */
    private Long id;

    /**
     * openId标识
     */
    private String openId;

    /**
     * 业务系统应用标识
     */
    private String productAppId;
    /**
     * 业务系统订单号
     */
    private Long productOrderNo;

    /**
     * 交易系统订单号【对于三方来说：商户订单】
     */
    private Long tradingOrderNo;

    /**
     * 第三方支付的交易号
     */
    private String transactionId;

    /**
     * 支付渠道【支付宝、微信、现金、免单挂账】
     */
    private String tradingChannel;

    /**
     * 交易类型【付款、退款、免单、挂账】
     */
    private String tradingType;

    /**
     * 交易单状态【DFK待付款,FKZ付款中,QXDD取消订单,YJS已结算,MD免单,GZ挂账】
     */
    private TradingStateEnum tradingState;

    /**
     * 收款人姓名
     */
    private String payeeName;

    /**
     * 收款人账户ID
     */
    private Long payeeId;

    /**
     * 付款人姓名
     */
    private String payerName;

    /**
     * 付款人Id
     */
    private Long payerId;

    /**
     * 交易金额
     */
    private BigDecimal tradingAmount;

    /**
     * 退款金额【付款后】
     */
    private BigDecimal refund;

    /**
     * 是否有退款：YES，NO
     */
    private String isRefund;

    /**
     * 第三方交易返回编码【最终确认交易结果】
     */
    private String resultCode;

    /**
     * 第三方交易返回提示消息【最终确认交易信息】
     */
    private String resultMsg;

    /**
     * 第三方交易返回信息json【分析交易最终信息】
     */
    private String resultJson;

    /**
     * 统一下单返回编码
     */
    private String placeOrderCode;

    /**
     * 统一下单返回信息
     */
    private String placeOrderMsg;

    /**
     * 统一下单返回信息json【用于生产二维码、Android ios唤醒支付等】
     */
    private String placeOrderJson;

    /**
     * 商户号
     */
    private Long enterpriseId;

    /**
     * 备注【订单门店，桌台信息】
     */
    private String memo;

    /**
     * 二维码base64数据
     */
    private String qrCode;

    /**
     * 是否有效
     */
    protected String enableFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
