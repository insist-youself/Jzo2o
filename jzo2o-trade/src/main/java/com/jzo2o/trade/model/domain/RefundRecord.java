package com.jzo2o.trade.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jzo2o.trade.enums.RefundStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author itcast
 * @Description：退款记录表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("refund_record")
public class RefundRecord implements Serializable {
    private static final long serialVersionUID = -3998253241655800061L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 交易系统订单号【对于三方来说：商户订单】
     */
    private Long tradingOrderNo;

    /**
     * 业务系统应用标识
     */
    private String productAppId;

    /**
     * 业务系统订单号
     */
    private Long productOrderNo;

    /**
     * 本次退款订单号
     */
    private Long refundNo;

    /**
     * 第三方支付的退款单号
     */
    private String refundId;

    /**
     * 商户号
     */
    private Long enterpriseId;

    /**
     * 退款渠道【支付宝、微信、现金】
     */
    private String tradingChannel;

    /**
     * 退款状态
     */
    private RefundStatusEnum refundStatus;

    /**
     * 返回编码
     */
    private String refundCode;

    /**
     * 返回信息
     */
    private String refundMsg;

    /**
     * 备注【订单门店，桌台信息】
     */
    private String memo;

    /**
     * 本次退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 原订单金额
     */
    private BigDecimal total;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
