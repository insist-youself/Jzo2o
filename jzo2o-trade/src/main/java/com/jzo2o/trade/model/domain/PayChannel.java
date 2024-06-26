package com.jzo2o.trade.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author itcast
 * @Description：交易渠道表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pay_channel")
public class PayChannel implements Serializable {
    private static final long serialVersionUID = -1452774366739615656L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 通道唯一标记
     */
    private String channelLabel;

    /**
     * 域名
     */
    private String domain;

    /**
     * 商户appid
     */
    private String appId;

    /**
     * 支付公钥
     */
    private String publicKey;

    /**
     * 商户私钥
     */
    private String merchantPrivateKey;

    /**
     * 其他配置
     */
    private String otherConfig;

    /**
     * AES混淆密钥
     */
    private String encryptKey;

    /**
     * 说明
     */
    private String remark;

    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 是否有效
     */
    protected String enableFlag;

    /**
     * 商户号
     */
    private Long enterpriseId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
