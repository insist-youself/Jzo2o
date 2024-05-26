package com.jzo2o.orders.manager.porperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author itcast
 */
@Data
@Component
@ConfigurationProperties(prefix = "jzo2o.trade")
public class TradeProperties {

    /**
     * 支付宝商户id
     */
    private Long aliEnterpriseId;

    /**
     * 微信支付商户id
     */
    private Long wechatEnterpriseId;
}